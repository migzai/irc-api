package com.ircclouds.irc.api.commands;

public class KickUserCmd implements ICommand
{
	private String channel;
	private String user;
	private String kickMsg;

	public KickUserCmd(String aChannel, String aNick, String aKickMessage)
	{
		user = aNick;
		channel = aChannel;
		kickMsg = aKickMessage;
	}

	public String asString()
	{
		return new StringBuffer().append("KICK").append(" ").append(channel).append(" ").append(user).append(" :").append(kickMsg).append("\r\n").toString();
	}
}
