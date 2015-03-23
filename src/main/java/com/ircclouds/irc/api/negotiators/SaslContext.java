package com.ircclouds.irc.api.negotiators;

import com.ircclouds.irc.api.negotiators.api.Relay;
import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;

/**
 * State Machine for the SASL authentication protocol.
 *
 * @author Danny van Heumen
 */
public class SaslContext
{
	private static final String AUTHENTICATE = "AUTHENTICATE ";
	private static final String AUTHENTICATE_ABORT = AUTHENTICATE + "*";

	/**
	 * IRC server relay instance.
	 */
	private final Relay relay;

	/**
	 * The current state in the SASL Context.
	 */
	private AbstractState state;

	/**
	 * Constructor.
	 * @param relay the IRC server relay instance
	 */
	public SaslContext(final Relay relay)
	{
		if (relay == null)
		{
			throw new NullPointerException("relay");
		}
		this.relay = relay;
		this.state = new InitialState();
	}

	/**
	 * Set the new current state of the context.
	 *
	 * @param state the new state
	 */
	private void setState(final AbstractState state)
	{
		if (state == null)
		{
			throw new NullPointerException("state");
		}
		this.state = state;
	}

	/**
	 * Initiate SASL authentication.
	 */
	public void init()
	{
		this.state.init(this);
	}

	/**
	 * Confirm suggested authentication mechanism.
	 *
	 * @param parameters the parameters
	 * @param authzid the authorization role id
	 * @param user the user id
	 * @param pass the password
	 */
	public void confirm(final String parameters, final String authzid, final String user, final String pass)
	{
		this.state.confirm(this, parameters, authzid, user, pass);
	}

	/**
	 * Confirm logged in state.
	 */
	public void loggedIn()
	{
		this.state.loggedIn(this);
	}

	/**
	 * Confirm successful authentication.
	 */
	public void success()
	{
		this.state.success(this);
	}

	/**
	 * Fail.
	 */
	public void fail()
	{
		this.state.fail(this);
	}

	/**
	 * Abort current authentication state.
	 */
	public void abort()
	{
		this.state.abort(this);
	}

	private static abstract class AbstractState
	{

		/**
		 * Initialize sasl state now that CAP REQ sasl request has been sent.
		 */
		abstract void init(final SaslContext context);

		/**
		 * Confirm proposed mechanism with optional max response length
		 * parameter.
		 *
		 * @param context current SASL context
		 * @param parameters optional max response length parameters
		 * @param authzid the authorization role
		 * @param user the user name
		 * @param pass the password
		 */
		abstract void confirm(SaslContext context, String parameters, String authzid, String user, String pass);

		/**
		 * Signal logged in.
		 *
		 * This method returns either the same state (if successful sasl was not
		 * yet confirmed) or new state if both messages have been confirmed.
		 * @param context the current SASL context
		 */
		abstract void loggedIn(SaslContext context);

		/**
		 * Signal successful SASL authentication.
		 *
		 * This method returns either the same state (if logged in was not yet
		 * confirmed) or new state if both messages have been confirmed.
		 * @param context the current SASL context
		 */
		abstract void success(SaslContext context);

		/**
		 * Signal authentication failure.
		 * @param context the current SASL context
		 */
		abstract void fail(SaslContext context);

		/**
		 * Signal aborting SASL authentication altogether.
		 * @param context the current SASL context
		 */
		abstract void abort(SaslContext context);
	}

	/**
	 * Initial state. Until ack is given and sasl negotiation can start.
	 * Initiation will send authentication mechanism proposal.
	 */
	private static final class InitialState extends AbstractState
	{

		@Override
		void init(final SaslContext context)
		{
			context.relay.send(AUTHENTICATE + "PLAIN");
			context.setState(new SaslInitiate());
		}

		@Override
		void confirm(final SaslContext context, final String parameters, final String authzid, final String user, final String pass)
		{
			throw new IllegalStateException("SASL not initiated. Awaiting initiation of request.");
		}

		@Override
		void loggedIn(final SaslContext context)
		{
			throw new IllegalStateException("SASL not initiated. Awaiting initiation of request.");
		}

		@Override
		void success(final SaslContext context)
		{
			throw new IllegalStateException("SASL not initiated. Awaiting initiation of request.");
		}

		@Override
		void fail(final SaslContext context)
		{
			// If we failed to CAP REQ sasl extension, then we cannot continue.
			abort(context);
		}

		@Override
		void abort(final SaslContext context)
		{
			context.setState(new InitialState());
		}
	}

	/**
	 * CAP REQ is acknowledged and mechanism proposal has been sent. Upon
	 * confirmation of the authentication mechanism we send the proper
	 * authentication credentials.
	 */
	private static final class SaslInitiate extends AbstractState
	{

		@Override
		void init(final SaslContext context)
		{
			throw new IllegalStateException("SASL already initialized. Awaiting acceptance of AUTHENTICATE proposal.");
		}

		@Override
		void confirm(final SaslContext context, final String parameters, final String authzid, final String user, final String pass)
		{
			context.relay.send(createAuthenticateMessage(authzid, user, pass));
			context.setState(new SaslConfirmed());
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

		/**
		 * Encode role, user, pass into SASL-supported encoding.
		 *
		 * @param authzid the authorization id
		 * @param user the user id
		 * @param pass the password
		 * @return Returns the SASL supported encoding.
		 */
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

		@Override
		void loggedIn(final SaslContext context)
		{
			throw new IllegalStateException("SASL not confirmed yet. Awaiting acceptance of AUTHENTICATE proposal.");
		}

		@Override
		void success(final SaslContext context)
		{
			throw new IllegalStateException("SASL not confirmed yet. Awaiting acceptance of AUTHENTICATE proposal.");
		}

		@Override
		void fail(final SaslContext context)
		{
			// Only PLAIN mechanism support and proposal fails, so abort.
			abort(context);
		}

		@Override
		void abort(final SaslContext context)
		{
			context.relay.send(AUTHENTICATE_ABORT);
			context.setState(new InitialState());
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

		private boolean loggedIn;
		private boolean successful;

		private SaslConfirmed()
		{
			this.loggedIn = false;
			this.successful = false;
		}

		@Override
		void init(final SaslContext context)
		{
			throw new IllegalStateException("SASL already initialized. Awaiting confirmation of successful log in.");
		}

		@Override
		void confirm(final SaslContext context, final String parameters, final String authzid, final String user, final String pass)
		{
		}

		@Override
		void loggedIn(final SaslContext context)
		{
			this.loggedIn = true;
		}

		@Override
		void success(final SaslContext context)
		{
			this.successful = true;
		}

		@Override
		void fail(final SaslContext context)
		{
			abort(context);
		}

		@Override
		void abort(final SaslContext context)
		{
			if (this.loggedIn || this.successful)
			{
				// log in confirmation or all-out success, nothing to abort
				return;
			}
			context.relay.send(AUTHENTICATE_ABORT);
			context.setState(new InitialState());
		}
	}
}
