package com.ircclouds.irc.api.domain;

import java.util.*;

/**
 * The IRC channel object that will be returned when an asynchronous joinChannel
 * succeeds.
 * 
 * The object stores the channel name, topic, channel modes, and a mapping of
 * all channel users and their statuses.
 * 
 * @author miguel@lebane.se
 * 
 */
public class WritableIRCChannel
{
	private String name;
	private WritableIRCTopic topic;

	private Map<WritableIRCUser, Set<IRCUserStatus>> usersStatuses = new LinkedHashMap<WritableIRCUser, Set<IRCUserStatus>>();
	private Set<ChannelMode> chanModes = new HashSet<ChannelMode>();

	public WritableIRCChannel(String aName)
	{
		this(aName, new WritableIRCTopic());
	}

	public WritableIRCChannel(String aName, WritableIRCTopic aTopic)
	{
		name = aName;
		topic = aTopic;
	}

	public void setName(String aName)
	{
		name = aName;
	}
	
	public String getName()
	{
		return name;
	}

	public WritableIRCTopic getTopic()
	{
		return topic;
	}

	public void setTopic(WritableIRCTopic aTopic)
	{
		topic = aTopic;
	}

	public void setModes(Set<ChannelMode> aModes)
	{
		chanModes = aModes;
	}

	public Set<ChannelMode> getModes()
	{
		return chanModes;
	}

	public Set<IRCUserStatus> getStatusesForUser(WritableIRCUser aUser)
	{
		return usersStatuses.get(aUser);
	}

	public Set<IRCUserStatus> addUser(WritableIRCUser aUser)
	{
		return usersStatuses.put(aUser, new HashSet<IRCUserStatus>());
	}

	public Set<IRCUserStatus> addUser(WritableIRCUser aUser, Set<IRCUserStatus> aStatus)
	{
		return usersStatuses.put(aUser, aStatus);
	}

	public Set<IRCUserStatus> removeUser(WritableIRCUser aUser)
	{
		return usersStatuses.remove(aUser);
	}

	public List<WritableIRCUser> getUsers()
	{
		return new ArrayList<WritableIRCUser>(usersStatuses.keySet());
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