package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

/**
 * 
 * @author
 * 
 */
public class ChanPartMessage implements IChannelMessage
{
	private String chanName;
	private IRCUser user;
	private String partMsg;

	public ChanPartMessage(String aChanName, IRCUser aUser)
	{
		this(aChanName, aUser, "");
	}
	
	public ChanPartMessage(String aChanName, IRCUser aUser, String aPartMsg)
	{
		chanName = aChanName;
		user = aUser;
		partMsg = aPartMsg;
	}
	
	public String getPartMsg()
	{
		return partMsg;
	}

	public String getChannelName()
	{
		return chanName;
	}

	public IRCUser getFromUser()
	{
		return user;
	}
}
