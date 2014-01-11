package com.ircclouds.irc.api.domain;

import java.util.*;

public class WritableIRCChannel extends IRCChannel
{
	public WritableIRCChannel(String aName)
	{
		super(aName);
	}

	public void setTopic(WritableIRCTopic aTopic)
	{
		topic = aTopic;
	}

	public void setModes(Set<ChannelMode> aModes)
	{
		chanModes = aModes;
	}

	public void setName(String aName)
	{
		name = aName;
	}

	public Set<IRCUserStatus> addUser(IRCUser aUser)
	{
		return usersStatuses.put(aUser, new HashSet<IRCUserStatus>());
	}

	public Set<IRCUserStatus> addUser(IRCUser aUser, Set<IRCUserStatus> aStatus)
	{
		return usersStatuses.put(aUser, aStatus);
	}

	public Set<IRCUserStatus> removeUser(IRCUser aUser)
	{
		return usersStatuses.remove(aUser);
	}
}