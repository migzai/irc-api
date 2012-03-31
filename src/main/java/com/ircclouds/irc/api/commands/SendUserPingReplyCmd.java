package com.ircclouds.irc.api.commands;

import com.ircclouds.irc.api.domain.messages.*;

public class SendUserPingReplyCmd implements ICommand
{
	private UserPong userPong;

	public SendUserPingReplyCmd(UserPong aUserPong)
	{
		userPong = aUserPong;
	}

	public String asString()
	{
		return userPong.toString();
	}
}
