package com.ircclouds.irc.api.domain;

import java.util.*;

/**
 * The IRC channel object that will be returned when an asynchronous joinChannel succeeds.
 * 
 * The object stores the channel name, topic, channel modes, and a mapping of all channel users and their statuses.
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
	
	public WritableIRCChannel()
	{
		this("");
	}

	public WritableIRCChannel(String aName)
	{
		this(aName, new WritableIRCTopic());
	}
	
	public WritableIRCChannel(String aName, WritableIRCTopic aTopic)
	{
		name = aName;
		topic = aTopic;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String aName)
	{
		name = aName;
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

	public Set<WritableIRCUser> getUsers()
	{
		return usersStatuses.keySet();
	}

	@Override
	public boolean equals(Object aObject)
	{
		if (aObject != null)
		{
			if (aObject instanceof WritableIRCChannel)
			{
				return ((WritableIRCChannel)aObject).getName().equals(name);
			}
			else if (aObject instanceof String)
			{
				return aObject.equals(name);
			}
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
}