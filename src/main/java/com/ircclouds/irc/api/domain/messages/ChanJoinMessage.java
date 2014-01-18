package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

/**
 * 
 * @author
 * 
 */
public class ChanJoinMessage implements IChannelMessage, IUserMessage
{
	private String chanName;
	private IRCUser user;

	public ChanJoinMessage(IRCUser aUser, String aChanName)
	{
		user = aUser;
		chanName = aChanName;
	}
	
	public String getChannelName()
	{
		return chanName;
	}

	public IRCUser getSource()
	{
		return user;
	}
}
