package com.ircclouds.irc.api.om;

import com.ircclouds.irc.api.domain.messages.*;

/**
 * Builder for building unknown messages.
 * 
 * This builder can be used in case of unknown or unsupported messages that
 * should be passed on unmodified.
 * 
 * @author Danny van Heumen
 */
public class UnknownMessageBuilder implements IBuilder<UnknownMessage>
{
	@Override
	public UnknownMessage build(String aMessage)
	{
		return new UnknownMessage(aMessage);
	}
}