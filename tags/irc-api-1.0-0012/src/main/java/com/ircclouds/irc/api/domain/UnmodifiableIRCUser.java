package com.ircclouds.irc.api.domain;

public class UnmodifiableIRCUser extends IRCUser
{
	private IRCUser user;
	
	public UnmodifiableIRCUser(IRCUser aUser)
	{
		user = aUser;
	}

	public String getNick()
	{
		return user.getNick();
	}

	public void setNick(String aNick)
	{
		throw new UnsupportedOperationException();
	}

	public String getHostname()
	{
		return user.getHostname();
	}

	public void setHostname(String aHostname)
	{
		throw new UnsupportedOperationException();
	}

	public String getIdent()
	{
		return user.getIdent();
	}

	public void setIdent(String aIdent)
	{
		throw new UnsupportedOperationException();
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
