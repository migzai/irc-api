package com.ircclouds.irc.api.negotiators.api;

/**
 * Relay instance for sending messages to an IRC server.
 *
 * This is mainly used during the Capability Negotiation phase where an IRC
 * connection is not fully established yet. This relay instance is a stripped
 * interface that allows sending of messages.
 *
 * @author Danny van Heumen
 */
public interface Relay
{

	/**
	 * Send a message to relay to the IRC server.
	 *
	 * @param msg the message to send
	 */
	void send(String msg);
}
