package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class NickMessage implements IUserMessage
{
	private WritableIRCUser user;
	private String newNick;
	
	public NickMessage(WritableIRCUser aUser, String aNewNick)
	{
		user = aUser;
		newNick = aNewNick;
	}
	
	public String getNewNick()
	{
		return newNick;
	}

	@Override
	public WritableIRCUser getSource()
	{
		return user;
	}
}
