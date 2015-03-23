package com.ircclouds.irc.api.negotiators;


import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.commands.*;
import com.ircclouds.irc.api.domain.messages.ServerNumericMessage;
import com.ircclouds.irc.api.domain.messages.interfaces.*;
import com.ircclouds.irc.api.listeners.VariousMessageListenerAdapter;
import com.ircclouds.irc.api.negotiators.api.Relay;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of a SASL negotiator. This negotiator will negotiate for the
 * 'sasl' extension. If the extension is acknowledged by the IRC server, it will
 * start the authentication procedure and authenticate the user with the
 * credentials provided in the constructor. The implementation currently only
 * supports authentication mechanism PLAIN.
 *
 * Other mechanisms available in context of IRC are:
 * - DH-BLOWFISH (https://github.com/ircv3/ircv3-specifications/blob/master/documentation/sasl-dh-blowfish.md)
 * - DH-AES (https://github.com/ircv3/ircv3-specifications/blob/master/documentation/sasl-dh-aes.md)
 * There appears to be some discussion on the actual value of these mechanisms,
 * because they do not protect against MITM attacks.
 *
 * Apart from that, key sizes may be smaller than the key sizes you get when
 * connecting to an IRC server via a TLS connection. For example, DH-AES is
 * based on AES-128, i.e. a 128 bit AES key. Therefore, it may be more secure to
 * connect with an IRC server over TLS (which may provide AES-256) and use the
 * PLAIN mechanism for authentication, which is already sent over a secure
 * connection.
 *
 * Also see http://kaniini.dereferenced.org/2014/12/26/do-not-use-DH-AES-or-DH-BLOWFISH.html.
 *
 * @author Danny van Heumen
 */
public class SaslNegotiator extends VariousMessageListenerAdapter implements CapabilityNegotiator
{
	private static final Logger LOG = LoggerFactory.getLogger(SaslNegotiator.class);

	private static final String SASL_CAPABILITY_ID = "sasl";

	private static final Pattern CAPABILITY_ACK = Pattern.compile("\\sCAP\\s+([^\\s]+)\\s+ACK\\s+:([\\w-_]+(?:\\s+[\\w-_]+)*)\\s*$", 0);
	private static final Pattern CAPABILITY_NAK = Pattern.compile("\\sCAP\\s+([^\\s]+)\\s+NAK");
	private static final Pattern AUTHENTICATE_CONFIRMATION = Pattern.compile("AUTHENTICATE\\s+(\\+)\\s*$", 0);

	// AUTHENTICATE numeric replies
	private static final int RPL_LOGGEDIN = 900;
	private static final int RPL_LOGGEDOUT = 901;
	private static final int ERR_NICKLOCKED = 902;
	private static final int RPL_SASLSUCCESS = 903;
	private static final int ERR_SASLFAIL = 904;
	private static final int ERR_SASLTOOLONG = 905;
	private static final int ERR_SASLABORTED = 906;
	private static final int ERR_SASLALREADY = 907;
	private static final int RPL_SASLMECHS = 908;

	private final String user;
	private final String pass;
	private final String authzid;

	private SaslContext state;
	private IRCApi irc;

	// TODO How to handle time-outs? (though not crucial since IRC server will also time-out)
	public SaslNegotiator(final String user, final String pass, final String authzid)
	{
		if (user == null)
		{
			throw new IllegalArgumentException("user cannot be null");
		}
		this.user = user;
		if (pass == null)
		{
			throw new IllegalArgumentException("pass cannot be null");
		}
		this.pass = pass;
		this.authzid = authzid;
	}

	@Override
	public CapCmd initiate(final IRCApi irc)
	{
		if (irc == null)
		{
			throw new IllegalArgumentException("irc instance is required");
		}
		this.irc = irc;
		this.state = new SaslContext(new Relay() {

			@Override
			public void send(final String msg)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("CLIENT: " + msg);
				}
				SaslNegotiator.this.irc.rawMessage(msg);
			}
		});
		return new CapReqCmd(SASL_CAPABILITY_ID);
	}

	@Override
	public void onMessage(IMessage msg)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("SERVER: " + msg.asRaw());
		}
		final String rawmsg = msg.asRaw();
		final Matcher capAck = CAPABILITY_ACK.matcher(rawmsg);
		final Matcher capNak = CAPABILITY_NAK.matcher(rawmsg);
		final Matcher confirmation = AUTHENTICATE_CONFIRMATION.matcher(rawmsg);
		try
		{
			if (capAck.find() && saslAcknowledged(capAck.group(2)))
			{
				this.state.init();
			}
			else if (capNak.find())
			{
				this.irc.rawMessage(new CapEndCmd().asString());
			}
			else if (confirmation.find())
			{
				this.state.confirm(confirmation.group(1), this.authzid, this.user, this.pass);
			}
			else
			{
				// IGNORING, currently ...
			}
		}
		catch (RuntimeException e)
		{
			LOG.error("Error occurred during CAP negotiation. Prematurely ending CAP negotiation phase and continuing IRC registration as is.", e);
			this.irc.rawMessage(new CapEndCmd().asString());
		}
	}

	private boolean saslAcknowledged(final String acknowledged)
	{
		final String[] caps = acknowledged.split("\\s+");
		for (String cap : caps)
		{
			if (SASL_CAPABILITY_ID.equals(cap))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void onServerNumericMessage(ServerNumericMessage msg)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("SERVER: " + msg.asRaw());
		}
		try
		{
			switch (msg.getNumericCode())
			{
			case RPL_LOGGEDIN:
				this.state.loggedIn();
				break;
			case RPL_SASLSUCCESS:
				this.state.success();
				this.irc.rawMessage(new CapEndCmd().asString());
				break;
			case ERR_SASLFAIL:
				this.state.fail();
				break;
			case ERR_NICKLOCKED:
				LOG.error("SASL account locked. Aborting authentication procedure.");
				this.state.abort();
				this.irc.rawMessage(new CapEndCmd().asString());
				break;
			case ERR_SASLABORTED:
			case ERR_SASLALREADY:
			case ERR_SASLTOOLONG:
				this.state.abort();
				this.irc.rawMessage(new CapEndCmd().asString());
				break;
			default:
				break;
			}
		}
		catch (RuntimeException e)
		{
			LOG.error("Error occurred during CAP negotiation. Ending CAP negotiation phase and continuing registration as is.", e);
			this.irc.rawMessage(new CapEndCmd().asString());
		}
	}
}
