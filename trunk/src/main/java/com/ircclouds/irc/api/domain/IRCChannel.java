package com.ircclouds.irc.api.domain;

import java.util.*;

/**
 * 
 * @author
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
		name = aName;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String aName)
	{
		name = aName;
	}

	public void addUser(IRCUser aUser, Set<IRCUserStatus> aStatus)
	{
		users.put(aUser, aStatus);
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
