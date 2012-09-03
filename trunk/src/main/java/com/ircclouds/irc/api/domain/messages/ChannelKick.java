package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;

public class ChannelKick extends ChannelPrivMsg
{
	private String kickedUser;
	
	public ChannelKick(IRCUser aFromUser, String aText, String aChanName, String aKickedUser)
	{
		super(aFromUser, aText, aChanName);
		
		kickedUser = aKickedUser;
	}
	
	public String getKickedUser()
	{
		return kickedUser;
	}
}
