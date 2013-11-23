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
public class IRCChannel
{
	private String name;
	private IRCTopic topic;
	
	private Map<IRCUser, Set<IRCUserStatus>> users = new LinkedHashMap<IRCUser, Set<IRCUserStatus>>();
	private List<ChannelMode> chanModes = new ArrayList<ChannelMode>();
	
	public IRCChannel()
	{
		this("");
	}

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

	public void setName(String aName)
	{
		name = aName;
	}

	public Set<IRCUserStatus> addUser(IRCUser aUser)
	{
		return users.put(aUser, new HashSet<IRCUserStatus>());
	}
	
	public Set<IRCUserStatus> addUser(IRCUser aUser, Set<IRCUserStatus> aStatus)
	{
		return users.put(aUser, aStatus);
	}

	public Set<IRCUserStatus> removeUser(IRCUser aUser)
	{
		return users.remove(aUser);
	}

	public IRCTopic getTopic()
	{
		return topic;
	}

	public void setTopic(IRCTopic aTopic)
	{
		topic = aTopic;
	}

	public Map<IRCUser, Set<IRCUserStatus>> getUsers()
	{
		return users;
	}

	public void setUsers(Map<IRCUser, Set<IRCUserStatus>> aUsers)
	{
		users = aUsers;
	}
	
	public List<ChannelMode> getModes()
	{
		return chanModes;
	}

	@Override
	public boolean equals(Object aObject)
	{
		if (aObject != null)
		{
			if (aObject instanceof IRCChannel)
			{
				return ((IRCChannel)aObject).getName().equals(name);
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
