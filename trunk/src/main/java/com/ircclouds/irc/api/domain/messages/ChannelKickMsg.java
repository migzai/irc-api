package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;

public class ChannelKickMsg extends ChannelPrivMsg
{
	private String kickedUser;
	
	public ChannelKickMsg(IRCUser aFromUser, String aText, String aChanName, String aKickedUser)
	{
		super(aFromUser, aText, aChanName);
		
		kickedUser = aKickedUser;
	}
	
	public String getKickedUser()
	{
		return kickedUser;
	}
}
