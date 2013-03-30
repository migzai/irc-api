package com.ircclouds.irc.api.filters;

import com.ircclouds.irc.api.domain.messages.interfaces.*;

/**
 * The message filter interface that can be used via {@link IRCApi}.
 * 
 * It allows filtering of {@link IMessage} and redirecting them to interested {@link TargetListeners}.
 * 
 * @author miguel@lebane.se
 *
 */
public interface IMessageFilter
{
	MessageFilterResult filter(IMessage aMessage);
	
	TargetListeners getTargetListeners();
}
