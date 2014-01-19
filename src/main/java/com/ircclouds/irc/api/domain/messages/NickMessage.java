package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class NickMessage implements IUserMessage
{
	private IRCUser user;
	private String newNick;
	
	public NickMessage(IRCUser aUser, String aNewNick)
	{
		user = aUser;
		newNick = aNewNick;
	}
	
	public String getNewNick()
	{
		return newNick;
	}

	@Override
	public IRCUser getSource()
	{
		return user;
	}
	
	@Override
	public String asRaw()
	{
		return new StringBuffer().append(user).append(" NICK :").append(newNick).toString();
	}	
}
