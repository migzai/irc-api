package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;

public class UserPrivMsg extends AbstractPrivMsg
{
	private String toUser;

	public UserPrivMsg(IRCUser aFromUser, String aToUser, String aText)
	{
		super(aFromUser, aText);

		toUser = aToUser;
	}

	public String getToUser()
	{
		return toUser;
	}

	@Override
	public String asRaw()
	{
		return new StringBuffer().append(":").append(fromUser).append(" PRIVMSG ").append(toUser).append(" :").append(text).toString();
	}
}