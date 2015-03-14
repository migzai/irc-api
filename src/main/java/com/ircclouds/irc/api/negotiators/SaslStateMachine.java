package com.ircclouds.irc.api.negotiators;

import com.ircclouds.irc.api.negotiators.util.Relay;
import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;

/**
 * State Machine for the SASL authentication protocol.
 *
 * @author Danny van Heumen
 */
public class SaslStateMachine
{

	private static final String AUTHENTICATE = "AUTHENTICATE ";
	private static final String AUTHENTICATE_ABORT = AUTHENTICATE + "*";

	public static interface State
	{

		/**
		 * Initialize sasl state now that CAP REQ sasl request has been sent.
		 *
		 * @return returns new state ready to commence sasl negotiation
		 */
		State init();

		/**
		 * Confirm proposed mechanism with optional max response length
		 * parameter.
		 *
		 * @param parameters optional max response length parameters
		 * @param authzid the authorization role
		 * @param user the user name
		 * @param pass the password
		 * @return returns new state after processing confirmation
		 */
		State confirm(String parameters, String authzid, String user, String pass);

		/**
		 * Signal logged in.
		 *
		 * This method returns either the same state (if successful sasl was not
		 * yet confirmed) or new state if both messages have been confirmed.
		 *
		 * @return returns new state after processing loggedIn signal
		 */
		State loggedIn();

		/**
		 * Signal successful SASL authentication.
		 *
		 * This method returns either the same state (if logged in was not yet
		 * confirmed) or new state if both messages have been confirmed.
		 *
		 * @return returns new state after processing successful sasl signal
		 */
		State success();

		/**
		 * Signal authentication failure.
		 *
		 * @return returns new state after processing failed attempt
		 */
		State fail();

		/**
		 * Signal aborting SASL authentication altogether.
		 *
		 * @return returns initial state after processing abort
		 */
		State abort();
	}

	private static abstract class AbstractState implements State
	{

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
				// This should not happen, since UTF-8 is defined as mandatory by the JVM standard.
				throw new IllegalArgumentException("Unsupported encoding specified.", e);
			}
		}
	}

	/**
	 * Initial state. Until ack is given and sasl negotiation can start.
	 * Initiation will send authentication mechanism proposal.
	 */
	public static final class InitialState extends AbstractState
	{

		private final Relay irc;

		public InitialState(final Relay irc)
		{
			if (irc == null)
			{
				throw new IllegalArgumentException("irc cannot be null");
			}
			this.irc = irc;
		}

		@Override
		public State init()
		{
			this.irc.send(AUTHENTICATE + "PLAIN");
			return new SaslInitiate(this.irc);
		}

		@Override
		public State confirm(final String parameters, final String authzid, final String user, final String pass)
		{
			throw new IllegalStateException("SASL not initiated. Awaiting initiation of request.");
		}

		@Override
		public State loggedIn()
		{
			throw new IllegalStateException("SASL not initiated. Awaiting initiation of request.");
		}

		@Override
		public State success()
		{
			throw new IllegalStateException("SASL not initiated. Awaiting initiation of request.");
		}

		@Override
		public State fail()
		{
			// If we failed to CAP REQ sasl extension, then we cannot continue.
			return abort();
		}

		@Override
		public State abort()
		{
			return new InitialState(this.irc);
		}
	}

	/**
	 * CAP REQ is acknowledged and mechanism proposal has been sent. Upon
	 * confirmation of the authentication mechanism we send the proper
	 * authentication credentials.
	 */
	private static final class SaslInitiate extends AbstractState
	{

		private final Relay irc;

		private SaslInitiate(final Relay irc)
		{
			if (irc == null)
			{
				throw new IllegalArgumentException("irc cannot be null");
			}
			this.irc = irc;
		}

		@Override
		public State init()
		{
			throw new IllegalStateException("SASL already initialized. Awaiting acceptance of AUTHENTICATE proposal.");
		}

		@Override
		public State confirm(final String parameters, final String authzid, final String user, final String pass)
		{
			this.irc.send(createAuthenticateMessage(authzid, user, pass));
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
		public State loggedIn()
		{
			throw new IllegalStateException("SASL not confirmed yet. Awaiting acceptance of AUTHENTICATE proposal.");
		}

		@Override
		public State success()
		{
			throw new IllegalStateException("SASL not confirmed yet. Awaiting acceptance of AUTHENTICATE proposal.");
		}

		@Override
		public State fail()
		{
			// Only PLAIN mechanism support and proposal fails, so abort.
			return abort();
		}

		@Override
		public State abort()
		{
			this.irc.send(AUTHENTICATE_ABORT);
			return new InitialState(this.irc);
		}
	}

	/**
	 * SASL authentication credentials have been sent. Upon receiving a logged
	 * in message and a successful sasl authentication message, we transition
	 * back to InitialState and we are now completely done. CAP END is sent to
	 * close CAP negotiation.
	 */
	private static final class SaslConfirmed extends AbstractState
	{

		private final Relay irc;
		private boolean loggedIn;
		private boolean successful;

		private SaslConfirmed(final Relay irc)
		{
			if (irc == null)
			{
				throw new IllegalArgumentException("irc cannot be null");
			}
			this.irc = irc;
			this.loggedIn = false;
			this.successful = false;
		}

		@Override
		public State init()
		{
			throw new IllegalStateException("SASL already initialized. Awaiting confirmation of successful log in.");
		}

		@Override
		public State confirm(final String parameters, final String authzid, final String user, final String pass)
		{
			return this;
		}

		@Override
		public State loggedIn()
		{
			this.loggedIn = true;
			return this;
		}

		@Override
		public State success()
		{
			this.successful = true;
			return this;
		}

		@Override
		public State fail()
		{
			return abort();
		}

		@Override
		public State abort()
		{
			if (this.loggedIn || this.successful)
			{
				// log in confirmation or all-out success, nothing to abort
				return this;
			}
			else
			{
				this.irc.send(AUTHENTICATE_ABORT);
				return new InitialState(this.irc);
			}
		}
	}
}
