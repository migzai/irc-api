package com.ircclouds.irc.api.domain;

public class WritableIRCUser extends IRCUser
{
	public WritableIRCUser(String aUser)
	{
		super(aUser);
	}

	public WritableIRCUser(String nick, String ident, String hostname)
	{
		super(nick, ident, hostname);
	}

	public void setNick(String aNick)
	{
		nick = aNick;
	}
}
