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
		if (aObject != null)
		{
			if (aObject instanceof WritableIRCUser)
			{
				return ((WritableIRCUser) aObject).getNick().equals(nick);
			}
			else if (aObject instanceof IRCUser)
			{
				return ((IRCUser) aObject).getNick().equals(nick);
			}
		}

		return nick.equals(aObject);
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
