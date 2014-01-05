package com.ircclouds.irc.api.domain;

public class IRCUser
{
	private WritableIRCUser user;
	
	public IRCUser(WritableIRCUser aUser)
	{
		user = aUser;
	}

	public String getNick()
	{
		return user.getNick();
	}

	public String getHostname()
	{
		return user.getHostname();
	}

	public String getIdent()
	{
		return user.getIdent();
	}

	@Override
	public boolean equals(Object aObject)
	{
		if (aObject != null)
		{
			return aObject.equals(user.getNick());
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		return user.getNick().hashCode();
	}
	
	public String toString()
	{
		return user.getNick() + "!" + user.getIdent() + "@" + user.getHostname();
	}
}
