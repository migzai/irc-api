package com.ircclouds.irc.api.domain;

import java.io.*;

import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class WritableIRCUser implements ISource, Serializable
{
	private String nick;
	private String hostname;
	private String ident;

	public WritableIRCUser()
	{
		this("");
	}

	public WritableIRCUser(String aNick)
	{
		this(aNick, "", "");
	}

	public WritableIRCUser(String aNick, String aIdent, String aHostname)
	{
		nick = aNick;
		ident = aIdent;
		hostname = aHostname;
	}	
	
	public String getNick()
	{
		return nick;
	}

	public void setNick(String aNick)
	{
		nick = aNick;
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
