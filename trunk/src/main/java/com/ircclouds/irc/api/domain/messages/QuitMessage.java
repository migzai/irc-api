package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

/**
 * 
 * @author
 * 
 */
public class QuitMessage implements IUserMessage
{
	private IRCUser user;
	private String quitMsg;

	public QuitMessage(IRCUser aFromUser, String aQuitMsg)
	{
		user = aFromUser;
		quitMsg = aQuitMsg;
	}
	
	public IRCUser getFromUser()
	{
		return user;
	}

	public String getQuitMsg()
	{
		return quitMsg;
	}
}
