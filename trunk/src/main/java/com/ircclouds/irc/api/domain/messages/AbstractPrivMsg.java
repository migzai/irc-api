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
	private IRCUser fromUser;
	private String text;

	public AbstractPrivMsg(IRCUser aFromUser, String aText)
	{
		fromUser = aFromUser;
		text = aText;
	}
	
	public IRCUser getFromUser()
	{
		return fromUser;
	}

	public String getText()
	{
		return text;
	}
}
