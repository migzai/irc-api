package com.ircclouds.irc.api.domain;

public final class IRCUserStatus
{
	private Character type;
	private Character prefix;
	private int priority;
	
	public IRCUserStatus(Character aType, Character aPrefix, int aPriority)
	{
		type = aType;
		prefix = aPrefix;
		priority = aPriority;
	}
	
	public Character getChanModeType()
	{
		return type;
	}

	public Character getPrefix()
	{
		return prefix;
	}

	public int getPriority()
	{
		return priority;
	}
	
	public String toString()
	{
		return type.toString() + " " + prefix + " " + priority;
	}
}
