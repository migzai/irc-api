package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.messages.interfaces.*;

/**
 * Unknown message class representing unknown/unidentified messages that have no
 * other way of being communicated.
 * 
 * @author Danny van Heumen
 */
public class UnknownMessage implements IMessage
{
	private final String msg;

	public UnknownMessage(final String rawMsg)
	{
		msg = rawMsg;
	}

	/**
	 * For the unknown message, given that nothing is known about this message,
	 * the source will always be NULL_SOURCE.
	 * 
	 * @return Always returns NULL_SOURCE as the source, since the unknown
	 *         message cannot be interpreted.
	 */
	@Override
	public ISource getSource()
	{
		return ISource.NULL_SOURCE;
	}

	/**
	 * Return the original, raw message string.
	 * 
	 * @return returns the original, raw message
	 */
	@Override
	public String asRaw()
	{
		return msg;
	}
}