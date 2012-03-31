package com.ircclouds.irc.api.domain.messages.interfaces;

import com.ircclouds.irc.api.domain.*;

/**
 * 
 * @author
 * 
 */
public interface IUserMessage extends IMessage
{
	IRCUser getFromUser();
}
