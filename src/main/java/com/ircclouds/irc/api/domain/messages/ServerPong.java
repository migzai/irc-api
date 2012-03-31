package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class ServerPong implements IMessage
{
	private static final String PONG = "PONG";
	private static final String SPACE = " ";
	private static final String COLUMN = ":";

	private String text;

	public ServerPong(String aReply)
	{
		text = aReply;
	}

	public String toString()
	{
		return PONG + SPACE + COLUMN + text;
	}
}
