package com.ircclouds.irc.api.negotiators;

import java.io.*;

import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.commands.*;
import com.ircclouds.irc.api.domain.messages.ServerNumericMessage;
import com.ircclouds.irc.api.domain.messages.interfaces.*;
import com.ircclouds.irc.api.listeners.VariousMessageListenerAdapter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;
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

	private static final String AUTHENTICATE = "AUTHENTICATE ";
	private static final String AUTHENTICATE_ABORT = AUTHENTICATE + "*";

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

	private State state;
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
		this.state = new InitialState();
	}

	@Override
	public CapCmd initiate(IRCApi irc)
	{
		if (irc == null)
		{
			throw new IllegalArgumentException("irc instance is required");
		}
		this.irc = irc;
		this.state = this.state.init(irc);
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
				this.state = this.state.ack();
			}
			else if (capNak.find())
			{
				this.state = this.state.fail();
			}
			else if (confirmation.find())
			{
				this.state = this.state.confirm(confirmation.group(1), this.authzid, this.user, this.pass);
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
				this.state = this.state.loggedIn();
				break;
			case RPL_SASLSUCCESS:
				this.state = this.state.success();
				break;
			case ERR_SASLFAIL:
				this.state = this.state.fail();
				break;
			case ERR_NICKLOCKED:
				LOG.error("SASL account locked. Aborting authentication procedure.");
				this.state = this.state.abort();
				break;
			case ERR_SASLABORTED:
			case ERR_SASLALREADY:
			case ERR_SASLTOOLONG:
				this.state = this.state.abort();
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

	private static abstract class State
	{

		/**
		 * Initialize sasl state now that CAP REQ sasl request has been sent.
		 *
		 * @param irc irc api instance
		 * @return returns new state ready to commence sasl negotiation
		 */
		abstract State init(IRCApi irc);

		/**
		 * Acknowledge that SASL negotiation is supported and can commence.
		 *
		 * @return returns new state
		 */
		abstract State ack();

		/**
		 * Confirm proposed mechanism with optional max response length
		 * parameter.
		 *
		 * @param parameters optional max response length parameters
		 * @return returns new state after processing confirmation
		 */
		abstract State confirm(String parameters, String authzid, String user, String pass);

		/**
		 * Signal logged in.
		 *
		 * This method returns either the same state (if successful sasl was not
		 * yet confirmed) or new state if both messages have been confirmed.
		 *
		 * @return returns new state after processing loggedIn signal
		 */
		abstract State loggedIn();

		/**
		 * Signal successful SASL authentication.
		 *
		 * This method returns either the same state (if logged in was not yet
		 * confirmed) or new state if both messages have been confirmed.
		 *
		 * @return returns new state after processing successful sasl signal
		 */
		abstract State success();

		/**
		 * Signal authentication failure.
		 *
		 * @return returns new state after processing failed attempt
		 */
		abstract State fail();

		/**
		 * Signal aborting SASL authentication altogether.
		 *
		 * @return returns initial state after processing abort
		 */
		abstract State abort();

		protected static void send(final IRCApi irc, final ICommand cmd)
		{
			send(irc, cmd.asString());
		}

		protected static void send(final IRCApi irc, final String msg)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("CLIENT: " + msg);
			}
			irc.rawMessage(msg);
		}

		protected static String encode(final String authzid, final String user, final String pass)
		{
			final StringBuilder response = new StringBuilder();
			if (authzid != null)
			{
				response.append(authzid);
			}
			response.append('\0').append(user).append('\0').append(pass);
			try
			{
				return Base64.encodeBase64String(response.toString().getBytes("UTF-8"));
			}
			catch (UnsupportedEncodingException e)
			{
				throw new IllegalArgumentException("Unsupported encoding specified.", e);
			}
		}
	}

	/**
	 * The initial state of the SASL negotiator. This is the state before
	 * anything has happened and neither CAP negotiation nor SASL authentication
	 * are in the picture yet. Upon initialization, we transition to a new state
	 * and we await acknowledgement of CAP REQ request.
	 */
	public static final class InitialState extends State
	{

		public InitialState()
		{
		}

		@Override
		State init(final IRCApi irc)
		{
			return new SaslRequested(irc);
		}

		@Override
		State ack()
		{
			throw new IllegalStateException("SASL not initialized.");
		}

		@Override
		State confirm(final String parameters, final String authzid, final String user, final String pass)
		{
			throw new IllegalStateException("SASL not initialized.");
		}

		@Override
		State loggedIn()
		{
			throw new IllegalStateException("SASL not initialized.");
		}

		@Override
		State success()
		{
			throw new IllegalStateException("SASL not initialized.");
		}

		@Override
		State fail()
		{
			// Silently fail as we are not in a state in which we can act on
			// anything specific. This could mean that we haven't started CAP
			// negotiation yet or that we are completely done with CAP
			// negotiation and SASL authentication.
			return this;
		}

		@Override
		State abort()
		{
			return this;
		}
	}

	/**
	 * A CAP REQ for sasl has been requested. In this state we await ACK of the
	 * sasl extension. Upon acknowledgement we immediately start SASL
	 * authentication by sending a mechanism proposal.
	 */
	public static final class SaslRequested extends State
	{

		private final IRCApi irc;

		private SaslRequested(final IRCApi irc)
		{
			if (irc == null)
			{
				throw new IllegalArgumentException("irc cannot be null");
			}
			this.irc = irc;
		}

		@Override
		State init(final IRCApi irc)
		{
			return this;
		}

		@Override
		State ack()
		{
			send(this.irc, AUTHENTICATE + "PLAIN");
			return new SaslAcknowledged(irc);
		}

		@Override
		State confirm(final String parameters, final String authzid, final String user, final String pass)
		{
			throw new IllegalStateException("SASL not acknowledged. Awaiting acknowledgement of request.");
		}

		@Override
		State loggedIn()
		{
			throw new IllegalStateException("SASL not acknowledged. Awaiting acknowledgement of request.");
		}

		@Override
		State success()
		{
			throw new IllegalStateException("SASL not acknowledged. Awaiting acknowledgement of request.");
		}

		@Override
		State fail()
		{
			// If we failed to CAP REQ sasl extension, then we cannot continue.
			return abort();
		}

		@Override
		State abort()
		{
			send(this.irc, new CapEndCmd());
			return new InitialState();
		}
	}

	/**
	 * CAP REQ is acknowledged and mechanism proposal has been sent. Upon
	 * confirmation of the authentication mechanism we send the proper
	 * authentication credentials.
	 */
	public static final class SaslAcknowledged extends State
	{

		private final IRCApi irc;

		private SaslAcknowledged(final IRCApi irc)
		{
			if (irc == null)
			{
				throw new IllegalArgumentException("irc cannot be null");
			}
			this.irc = irc;
		}

		@Override
		State init(final IRCApi irc)
		{
			throw new IllegalStateException("SASL already initialized. Awaiting acceptance of AUTHENTICATE proposal.");
		}

		@Override
		State ack()
		{
			// Not a big issue that we acknowledge twice, since we haven't gone further yet.
			return this;
		}

		@Override
		State confirm(final String parameters, final String authzid, final String user, final String pass)
		{
			send(this.irc, createAuthenticateMessage(authzid, user, pass));
			return new SaslConfirmed(this.irc);
		}

		private String createAuthenticateMessage(final String authzid, final String user, final String pass)
		{
			final String msg = AUTHENTICATE + encode(authzid, user, pass);
			if (msg.length() <= 400)
			{
				return msg;
			}
			else
			{
				return msg.substring(0, 400);
			}
		}

		@Override
		State loggedIn()
		{
			throw new IllegalStateException("SASL not confirmed yet. Awaiting acceptance of AUTHENTICATE proposal.");
		}

		@Override
		State success()
		{
			throw new IllegalStateException("SASL not confirmed yet. Awaiting acceptance of AUTHENTICATE proposal.");
		}

		@Override
		State fail()
		{
			// Only PLAIN mechanism support and proposal fails, so abort.
			return abort();
		}

		@Override
		State abort()
		{
			send(this.irc, new CapEndCmd());
			return new InitialState();
		}
	}

	/**
	 * SASL authentication credentials have been sent. Upon receiving a logged
	 * in message and a successful sasl authentication message, we transition
	 * back to InitialState and we are now completely done. CAP END is sent to
	 * close CAP negotiation.
	 */
	public static final class SaslConfirmed extends State
	{

		private final IRCApi irc;
		private boolean loggedIn;
		private boolean successful;

		private SaslConfirmed(final IRCApi irc)
		{
			if (irc == null)
			{
				throw new IllegalArgumentException("irc cannot be null");
			}
			this.irc = irc;
		}

		@Override
		State init(final IRCApi irc)
		{
			throw new IllegalStateException("SASL already initialized. Awaiting confirmation of successful log in.");
		}

		@Override
		State ack()
		{
			throw new IllegalStateException("SASL already acknowledged. Awaiting confirmation of successful log in.");
		}

		@Override
		State confirm(final String parameters, final String authzid, final String user, final String pass)
		{
			return this;
		}

		@Override
		State loggedIn()
		{
			this.loggedIn = true;
			return process();
		}

		@Override
		State success()
		{
			this.successful = true;
			return process();
		}

		private State process()
		{
			if (this.loggedIn && this.successful)
			{
				send(this.irc, new CapEndCmd());
				return new InitialState();
			}
			else
			{
				return this;
			}
		}

		@Override
		State fail()
		{
			return abort();
		}

		@Override
		State abort()
		{
			reset();
			send(this.irc, new CapEndCmd());
			return new InitialState();
		}

		private void reset()
		{
			this.loggedIn = false;
			this.successful = false;
		}
	}
}
