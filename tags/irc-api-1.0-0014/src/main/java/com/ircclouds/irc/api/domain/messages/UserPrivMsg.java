package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;

public class UserPrivMsg extends AbstractPrivMsg
{
	private String toUser;

	public UserPrivMsg(WritableIRCUser aFromUser, String aToUser, String aText)
	{
		super(aFromUser, aText);
		
		toUser = aToUser;
	}

	public String getToUser()
	{
		return toUser;
	}	
}
