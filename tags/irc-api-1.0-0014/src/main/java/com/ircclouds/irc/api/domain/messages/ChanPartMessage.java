package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

/**
 * 
 * @author
 * 
 */
public class ChanPartMessage implements IChannelMessage, IUserMessage
{
	private String chanName;
	private WritableIRCUser user;
	private String partMsg;

	public ChanPartMessage(String aChanName, WritableIRCUser aUser)
	{
		this(aChanName, aUser, "");
	}
	
	public ChanPartMessage(String aChanName, WritableIRCUser aUser, String aPartMsg)
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

	public WritableIRCUser getSource()
	{
		return user;
	}
}
