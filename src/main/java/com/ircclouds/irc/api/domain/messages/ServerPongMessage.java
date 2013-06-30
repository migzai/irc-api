package com.ircclouds.irc.api.domain.messages;

public class ServerPongMessage
{
	private static final String PONG = "PONG";
	private static final String SPACE = " ";
	private static final String COLUMN = ":";

	private String text;

	public ServerPongMessage(String aReply)
	{
		text = aReply;
	}

	public String toString()
	{
		return PONG + SPACE + COLUMN + text;
	}
}
