package com.ircclouds.irc.api;

import com.ircclouds.irc.api.commands.*;
import com.ircclouds.irc.api.listeners.*;

/**
 * IRC v3.1 capability negotiation.
 *
 * <p>
 * See https://github.com/ircv3/ircv3-specifications for IRC v3 specifications.
 * </p>
 *
 * @author Danny van Heumen
 */
public interface CapabilityNegotiator extends IMessageListener {

	/**
	 * Initiate the negotiator with the provided irc instance.
	 *
	 * <p>
	 * Initiate the negotiator and return the initial CAP negotiation message
	 * that should be sent back to the server.
	 * </p>
	 *
	 * <p>
	 * The IRCApi instance should be stored for later use during negotiations in
	 * order to respond to incoming CAP negoation messages.
	 * </p>
	 *
	 * @param irc
	 *            the current IRCApi instance
	 * @return returns initialization command for CAP negotiation
	 */
	CapCmd initiate(IRCApi irc);
}
