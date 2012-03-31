package com.ircclouds.irc.api.commands;

public class SendVersionReplyCmd implements ICommand
{
	private static final String VERSION = "IRCApi v0.1 by migz";

	private String nickname;

	public SendVersionReplyCmd(String aNick)
	{
		nickname = aNick;
	}

	public String asString()
	{
		return "NOTICE " + nickname + " :" + VERSION;
	}
}
