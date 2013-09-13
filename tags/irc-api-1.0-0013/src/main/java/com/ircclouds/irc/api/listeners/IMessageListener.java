package com.ircclouds.irc.api.listeners;

import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

/**
 * The IRC message listener interface that can be registered via {@link IRCApi}.
 * 
 * @author miguel@lebane.se
 *
 */
public interface IMessageListener
{
	void onMessage(IMessage aMessage);
}
