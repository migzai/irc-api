package com.ircclouds.irc.api.negotiators.capabilities;

import com.ircclouds.irc.api.domain.messages.ServerNumericMessage;
import com.ircclouds.irc.api.listeners.VariousMessageListenerAdapter;
import com.ircclouds.irc.api.negotiators.CompositeNegotiator;
import com.ircclouds.irc.api.negotiators.SaslStateMachine;
import com.ircclouds.irc.api.negotiators.util.Relay;
import com.ircclouds.irc.api.om.ServerMessageBuilder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SASL capability.
 *
 * The implementation of SASL capability using IRC server conversation in order
 * to do actual SASL authentication after the capability has been confirmed.
 *
 * @author Danny van Heumen
 */
public class SaslCapability extends VariousMessageListenerAdapter
		implements CompositeNegotiator.Capability
{
	/**
	 * Logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(SaslCapability.class);

	/**
	 * Capability ID for SASL.
	 */
	private static final String CAP_ID = "sasl";

	/**
	 * Pattern for authentication mechanism confirmation.
	 */
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

	/**
	 * Server Numeric Message builder.
	 */
	private static final ServerMessageBuilder SERVER_MSG_BUILDER = new ServerMessageBuilder();

	/**
	 * Negotiated capability state.
	 */
	private final boolean enable;

	/**
	 * Authorization role id.
	 */
	private final String authzId;

	/**
	 * User name.
	 */
	private final String user;

	/**
	 * Password.
	 */
	private final String pass;

	/**
	 * The SASL protocol state.
	 */
	private SaslStateMachine.State state;

	/**
	 * Constructor.
	 *
	 * @param enable <tt>true</tt> to negotiate enabling, <tt>false</tt> to
	 * negotiate disabling
	 * @param authzid (optional) authorization role id
	 * @param user username
	 * @param pass password
	 */
	public SaslCapability(final boolean enable, final String authzid, final String user, final String pass)
	{
		this.enable = enable;
		this.authzId = authzid;
		if (this.enable && user == null)
		{
			throw new NullPointerException("user");
		}
		this.user = user;
		if (this.enable && pass == null)
		{
			throw new NullPointerException("pass");
		}
		this.pass = pass;
	}

	@Override
	public String getId()
	{
		return CAP_ID;
	}

	@Override
	public boolean enable()
	{
		return this.enable;
	}

	@Override
	public boolean converse(Relay relay, String msg)
	{
		if (!this.enable)
		{
			// Nothing to do, since we're negotiating disabling sasl. By now it
			// is acknowledged that it is disabled, so we're done here.
			return false;
		}
		if (msg == null)
		{
			this.state = new SaslStateMachine.InitialState(relay).init();
			return true;
		}
		final Matcher confirmation = AUTHENTICATE_CONFIRMATION.matcher(msg);
		if (confirmation.find())
		{
			this.state = this.state.confirm(confirmation.group(1), this.authzId, this.user, this.pass);
			return true;
		}
		else if (isServerNumericMessage(msg))
		{
			final ServerNumericMessage numMsg = SERVER_MSG_BUILDER.build(msg);
			switch (numMsg.getNumericCode())
			{
			case RPL_LOGGEDIN:
				this.state = this.state.loggedIn();
				return true;
			case RPL_SASLSUCCESS:
				this.state = this.state.success();
				return false;
			case ERR_SASLFAIL:
				this.state = this.state.fail();
				// FIXME not sure if we receive another message after fail
				return true;
			case ERR_NICKLOCKED:
				LOG.error("SASL account locked. Aborting authentication procedure.");
				this.state = this.state.fail();
				// FIXME not sure if we receive another message after fail
				return true;
			case ERR_SASLABORTED:
			case ERR_SASLALREADY:
			case ERR_SASLTOOLONG:
				this.state = this.state.abort();
				return false;
			default:
				LOG.warn("Unsupported numeric message: " + msg);
				// FIXME assuming that we receive another (useful) message
				return true;
			}
		}
		else
		{
			LOG.warn("Unknown message, not handling: " + msg);
			return true;
		}
	}

	private boolean isServerNumericMessage(final String aMsg)
	{
		String[] parts = aMsg.split(" ");
		if (parts.length <= 1)
		{
			return false;
		}
		try
		{
			Integer.parseInt(parts[1]);
			return true;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}
}
