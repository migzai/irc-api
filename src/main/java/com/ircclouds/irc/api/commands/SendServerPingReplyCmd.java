package com.ircclouds.irc.api.commands;

import com.ircclouds.irc.api.domain.messages.*;

public class SendServerPingReplyCmd implements ICommand
{
	private ServerPong pong;

	public SendServerPingReplyCmd(ServerPong aPong)
	{
		pong = aPong;
	}

	public String asString()
	{
		return pong.toString();
	}

}
