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
	private WritableIRCUser user;

	public ChanJoinMessage(WritableIRCUser aUser, String aChanName)
	{
		user = aUser;
		chanName = aChanName;
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
