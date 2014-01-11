package com.ircclouds.irc.api.domain;

import java.io.*;

import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class IRCUser implements ISource, Serializable
{
	String nick;
	String hostname;
	String ident;

	public IRCUser()
	{
		this("");
	}

	public IRCUser(String aNick)
	{
		this(aNick, "", "");
	}

	public IRCUser(String aNick, String aIdent, String aHostname)
	{
		nick = aNick;
		ident = aIdent;
		hostname = aHostname;
	}

	public String getNick()
	{
		return nick;
	}

	public String getHostname()
	{
		return hostname;
	}

	public String getIdent()
	{
		return ident;
	}

	@Override
	public boolean equals(Object aObject)
	{
		if (aObject instanceof IRCUser)
		{
			return nick.equals(((IRCUser) aObject).getNick());
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		return nick.hashCode();
	}

	public String toString()
	{
		return nick + "!" + ident + "@" + hostname;
	}
}
