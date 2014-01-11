package com.ircclouds.irc.api.domain;

import java.util.*;

import com.ircclouds.irc.api.utils.*;

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

	public void addUser(IRCUser aUser)
	{
		addUser(aUser, new SynchronizedUnmodifiableSet<IRCUserStatus>(new HashSet<IRCUserStatus>()));
	}

	public void addUser(IRCUser aUser, Set<IRCUserStatus> aStatus)
	{
		users.addElement(aUser);
		usersStatuses.put(aUser, aStatus);
	}

	public Set<IRCUserStatus> removeUser(IRCUser aUser)
	{
		users.removeElement(aUser);
		return usersStatuses.remove(aUser);
	}
}