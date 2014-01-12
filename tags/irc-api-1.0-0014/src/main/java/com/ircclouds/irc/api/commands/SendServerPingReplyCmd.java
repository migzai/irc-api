package com.ircclouds.irc.api.commands;

import com.ircclouds.irc.api.domain.messages.*;

public class SendServerPingReplyCmd implements ICommand
{
	private ServerPongMessage pong;

	public SendServerPingReplyCmd(ServerPongMessage aPong)
	{
		pong = aPong;
	}

	public String asString()
	{
		return pong.toString();
	}

}
