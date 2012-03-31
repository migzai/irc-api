package com.ircclouds.irc.api.commands;

public class KickUserCmd implements ICommand
{
	private String user;
	private String channel;
	private String kickMsg;

	public KickUserCmd(String aUserName, String aChannel, String aKickMessage)
	{
		user = aUserName;
		channel = aChannel;
		kickMsg = aKickMessage;
	}

	public String asString()
	{
		return new StringBuffer().append("KICK").append(" ").append(channel).append(" ").append(user).append(" :").append(kickMsg).append("\r\n").toString();
	}

}
