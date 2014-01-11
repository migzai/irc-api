package com.ircclouds.irc.api.domain;

import java.io.*;
import java.util.*;

import com.ircclouds.irc.api.utils.*;

public class IRCChannel implements Serializable
{
	/**
	 * The IRC channel object that will be returned when an asynchronous
	 * joinChannel succeeds.
	 * 
	 * The object stores the channel name, topic, channel modes, and a mapping
	 * of all channel users and their statuses.
	 * 
	 * @author miguel@lebane.se
	 * 
	 */

	String name;
	IRCTopic topic;
	Set<ChannelMode> chanModes = new SynchronizedUnmodifiableSet<ChannelMode>(new HashSet<ChannelMode>());
	SynchronizedUnmodifiableList<IRCUser> users = new SynchronizedUnmodifiableList<IRCUser>(new ArrayList<IRCUser>());
	Map<IRCUser, Set<IRCUserStatus>> usersStatuses = new LinkedHashMap<IRCUser, Set<IRCUserStatus>>();
	
	public IRCChannel(String aName)
	{
		this(aName, new IRCTopic());
	}

	public IRCChannel(String aName, IRCTopic aTopic)
	{
		name = aName;
		topic = aTopic;
	}

	public String getName()
	{
		return name;
	}

	public IRCTopic getTopic()
	{
		return topic;
	}

	public Set<ChannelMode> getModes()
	{
		return chanModes;
	}

	public Set<IRCUserStatus> getStatusesForUser(IRCUser aUser)
	{
		return usersStatuses.get(aUser);
	}

	public List<IRCUser> getUsers()
	{
		return users;
	}

	@Override
	public boolean equals(Object aObject)
	{
		if (aObject != null && aObject instanceof WritableIRCChannel)
		{
			return ((WritableIRCChannel) aObject).getName().equals(name);
		}

		return name.equals(aObject);
	}

	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
}
