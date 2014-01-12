package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;

public class ChannelKick extends ChannelPrivMsg
{
	private String kickedNick;
	
	public ChannelKick(WritableIRCUser aFromUser, String aText, String aChanName, String aKickedNick)
	{
		super(aFromUser, aText, aChanName);
		
		kickedNick = aKickedNick;
	}
	
	public String getKickedNickname()
	{
		return kickedNick;
	}
}
