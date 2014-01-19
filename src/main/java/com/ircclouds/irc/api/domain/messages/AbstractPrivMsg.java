package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

/**
 * 
 * @author
 * 
 */
public abstract class AbstractPrivMsg implements IUserMessage, IHasText
{
	IRCUser fromUser;
	String text;

	public AbstractPrivMsg(IRCUser aFromUser, String aText)
	{
		fromUser = aFromUser;
		text = aText;
	}
	
	public IRCUser getSource()
	{
		return fromUser;
	}

	public String getText()
	{
		return text;
	}
}
