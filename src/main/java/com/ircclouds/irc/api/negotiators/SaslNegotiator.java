package com.ircclouds.irc.api.negotiators;

import java.io.*;

import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.commands.*;
import com.ircclouds.irc.api.domain.messages.ServerNumericMessage;
import com.ircclouds.irc.api.domain.messages.interfaces.*;
import com.ircclouds.irc.api.listeners.VariousMessageListenerAdapter;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaslNegotiator extends VariousMessageListenerAdapter implements CapabilityNegotiator {
	private static final Logger LOG = LoggerFactory.getLogger(SaslNegotiator.class);
	private static final String AUTHENTICATE = "AUTHENTICATE ";
	private static final String AUTHENTICATE_ABORT = "AUTHENTICATE *\r\n";

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

	// TODO how to handle time-outs?
	// TODO move more intelligence into the state implementations to the point
	// where negotiator only passes on information and does not make intelligent
	// decisions anymore
	public SaslNegotiator(final String user, final String pass, final String authzid) {
		if (user == null) {
			throw new IllegalArgumentException("user cannot be null");
		}
		this.user = user;
		if (pass == null) {
			throw new IllegalArgumentException("pass cannot be null");
		}
		this.pass = pass;
		this.authzid = authzid;
		this.state = new InitialState();
	}

	@Override
	public CapCmd initiate(IRCApi irc) {
		if (irc == null) {
			throw new IllegalArgumentException("irc instance is required");
		}
		this.irc = irc;
		this.state = this.state.init();
		return new CapReqCmd("sasl");
	}

	@Override
	public void onMessage(IMessage msg) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("SERVER: " + msg.asRaw());
		}
		try {
			// TODO implement CAP command builder with specialized message type
			if (msg.asRaw().contains("CAP * ACK :sasl")) {
				// FIXME convert to regex
				// FIXME support both <nick> and *
				this.state = this.state.ack();
				final StringBuilder reply = new StringBuilder(AUTHENTICATE);
				for (String mechanism : this.state.mechanisms()) {
					reply.append(mechanism).append(" ");
				}
				send(reply);
			} else if (msg.asRaw().contains(AUTHENTICATE + "+")) {
				this.state = this.state.confirm("+");
				final String challenge = this.state.credentials(this.authzid, this.user, this.pass);
				send(AUTHENTICATE + challenge);
			} else {
				// IGNORING, currently ...
			}
		} catch (RuntimeException e) {
			LOG.error("Error occurred during CAP negotiation. Ending CAP negotiation phase and continuing registration as is.", e);
			send(new CapEndCmd());
		}
	}

	@Override
	public void onServerNumericMessage(ServerNumericMessage msg) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("SERVER: " + msg.asRaw());
		}
		try {
			final int code = msg.getNumericCode();
			final State previous = this.state;
			switch (code) {
				case RPL_LOGGEDIN:
					this.state = this.state.loggedIn();
					if (checkEndStateTransition(previous)) {
						send(new CapEndCmd());
					}
					break;
				case RPL_SASLSUCCESS:
					this.state = this.state.success();
					if (checkEndStateTransition(previous)) {
						send(new CapEndCmd());
					}
					break;
				case ERR_SASLFAIL:
					this.state = this.state.fail();
					// FIXME next attempt or abort?
					break;
				case ERR_SASLABORTED:
					this.state = this.state.abort();
					send(new CapEndCmd());
					break;
				case ERR_SASLALREADY:
					// currently, sasl negotiation states do not have any kind of
					// internal persistent state, so we can simply abort as it will
					// bring us back to InitialState
					this.state = this.state.abort();
					send(new CapEndCmd());
					break;
				default:
					break;
			}
		} catch (RuntimeException e) {
			LOG.error("Error occurred during CAP negotiation. Ending CAP negotiation phase and continuing registration as is.", e);
			send(new CapEndCmd());
		}
	}

	private boolean checkEndStateTransition(final State previous) {
		return previous != this.state
				&& previous instanceof SaslConfirmed
				&& this.state instanceof InitialState;
	}

	private void send(final ICommand cmd) {
		send(cmd.asString());
	}

	private void send(final StringBuilder msg) {
		send(msg.toString());
	}

	private void send(final String msg) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("CLIENT: " + msg);
		}
		this.irc.rawMessage(msg);
	}

	private static abstract class State {
		/**
		 * Initialize sasl state now that CAP REQ sasl request has been sent.
		 *
		 * @return returns new state ready to commence sasl negotiation
		 */
		abstract State init();
		/**
		 * Acknowledge that SASL negotiation is supported and can commence.
		 *
		 * @return returns new state
		 */
		abstract State ack();
		/**
		 * Query for supported mechanisms.
		 *
		 * @return returns array of supported mechanisms
		 */
		abstract String[] mechanisms();
		/**
		 * Confirm proposed mechanism with optional max response length
		 * parameter.
		 *
		 * @param parameters optional max response length parameters
		 * @return returns new state after processing confirmation
		 */
		abstract State confirm(String parameters);
		/**
		 * Generate credentials response based on provided data.
		 *
		 * @param authz authorization role
		 * @param user user id
		 * @param pass password
		 * @return returns composed credentials response
		 */
		abstract String credentials(String authz, String user, String pass);
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
	}

	private static final class InitialState extends State {

		@Override
		State init() {
			return new SaslRequested();
		}

		@Override
		State ack() {
			throw new IllegalStateException("SASL not initialized.");
		}

		@Override
		String[] mechanisms() {
			throw new IllegalStateException("SASL not initialized.");
		}

		@Override
		State confirm(final String parameters) {
			throw new IllegalStateException("SASL not initialized.");
		}

		@Override
		String credentials(final String authz, final String user, final String pass) {
			throw new IllegalStateException("SASL not initialized.");
		}

		@Override
		State loggedIn() {
			throw new IllegalStateException("SASL not initialized.");
		}

		@Override
		State success() {
			throw new IllegalStateException("SASL not initialized.");
		}

		@Override
		State fail() {
			throw new IllegalStateException("SASL not initialized.");
		}

		@Override
		State abort() {
			return this;
		}
	}

	private static final class SaslRequested extends State {

		@Override
		State init() {
			return this;
		}

		@Override
		State ack() {
			return new SaslAcknowledged();
		}

		@Override
		String[] mechanisms() {
			throw new IllegalStateException("SASL not acknowledged. Awaiting acknowledgement of request.");
		}

		@Override
		State confirm(final String parameters) {
			throw new IllegalStateException("SASL not acknowledged. Awaiting acknowledgement of request.");
		}

		@Override
		String credentials(final String authz, final String user, final String pass) {
			throw new IllegalStateException("SASL not acknowledged. Awaiting acknowledgement of request.");
		}

		@Override
		State loggedIn() {
			throw new IllegalStateException("SASL not acknowledged. Awaiting acknowledgement of request.");
		}

		@Override
		State success() {
			throw new IllegalStateException("SASL not acknowledged. Awaiting acknowledgement of request.");
		}

		@Override
		State fail() {
			throw new IllegalStateException("SASL not acknowledged. Awaiting acknowledgement of request.");
		}

		@Override
		State abort() {
			return new InitialState();
		}
	}

	private static final class SaslAcknowledged extends State {

		@Override
		State init() {
			throw new IllegalStateException("SASL already initialized. Awaiting acceptance of AUTHENTICATE proposal.");
		}

		@Override
		State ack() {
			// Not a big issue that we acknowledge twice, since we haven't gone further yet.
			return this;
		}

		@Override
		String[] mechanisms() {
			return new String[] {"PLAIN"};
		}

		@Override
		State confirm(final String parameters) {
			return new SaslConfirmed();
		}

		@Override
		String credentials(final String authz, final String user, final String pass) {
			throw new IllegalStateException("SASL not confirmed yet. Awaiting acceptance of AUTHENTICATE proposal.");
		}

		@Override
		State loggedIn() {
			throw new IllegalStateException("SASL not confirmed yet. Awaiting acceptance of AUTHENTICATE proposal.");
		}

		@Override
		State success() {
			throw new IllegalStateException("SASL not confirmed yet. Awaiting acceptance of AUTHENTICATE proposal.");
		}

		@Override
		State fail() {
			throw new IllegalStateException("SASL not confirmed yet. Awaiting acceptance of AUTHENTICATE proposal.");
		}

		@Override
		State abort() {
			return new InitialState();
		}
	}

	private static final class SaslConfirmed extends State {
		private boolean loggedIn;
		private boolean successful;

		@Override
		State init() {
			throw new IllegalStateException("SASL already initialized. Awaiting confirmation of successful log in.");
		}

		@Override
		State ack() {
			throw new IllegalStateException("SASL already acknowledged. Awaiting confirmation of successful log in.");
		}

		@Override
		String[] mechanisms() {
			throw new IllegalStateException("SASL already acknowledged. Awaiting confirmation of successful log in.");
		}

		@Override
		State confirm(final String parameters) {
			// FIXME store authentication requirement parameters
			return this;
		}

		@Override
		String credentials(final String authz, final String user, final String pass) {
			return encode(authz, user, pass);
		}

		private String encode(final String authzid, final String user, final String pass) {
			final StringBuilder response = new StringBuilder();
			if (authzid != null) {
				response.append(authzid);
			}
			response.append('\u0000').append(user).append('\u0000').append(pass);
			try {
				return Base64.encodeBase64String(response.toString().getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				throw new IllegalArgumentException("Unsupported encoding specified.", e);
			}
		}

		@Override
		State loggedIn() {
			this.loggedIn = true;
			return next();
		}

		@Override
		State success() {
			this.successful = true;
			return next();
		}

		private State next() {
			return this.loggedIn && this.successful ? new InitialState() : this;
		}

		@Override
		State fail() {
			reset();
			return this;
		}

		@Override
		State abort() {
			reset();
			return new InitialState();
		}

		private void reset() {
			this.loggedIn = false;
			this.successful = false;
		}
	}
}
