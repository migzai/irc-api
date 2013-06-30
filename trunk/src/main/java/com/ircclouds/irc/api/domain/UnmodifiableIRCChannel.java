package com.ircclouds.irc.api.domain;

import java.util.*;
import java.util.Map.Entry;

public class UnmodifiableIRCChannel extends IRCChannel
{
	private IRCChannel channel;
	
	public UnmodifiableIRCChannel(IRCChannel aChannel)
	{
		channel = aChannel;
	}
	
	public String getName()
	{
		return channel.getName();
	}

	public IRCTopic getTopic()
	{
		return channel.getTopic();
	}
	
	public List<ChannelMode> getModes()
	{
		return channel.getModes();
	}
	
	public void setName(String aName)
	{
		 throw new UnsupportedOperationException();
	}

	public Set<IRCUserStatus> addUser(IRCUser aUser, Set<IRCUserStatus> aStatus)
	{
		 throw new UnsupportedOperationException();
	}

	public Set<IRCUserStatus> removeUser(IRCUser aUser)
	{
		 throw new UnsupportedOperationException();
	}
	
	public void setTopic(IRCTopic aTopic)
	{
		 throw new UnsupportedOperationException();
	}
	
	public void setUsers(Map<IRCUser, Set<IRCUserStatus>> aUsers)
	{
		 throw new UnsupportedOperationException();
	}
	
	public Map<IRCUser, Set<IRCUserStatus>> getUsers()
	{
		Map<IRCUser, Set<IRCUserStatus>> _map = new HashMap<IRCUser, Set<IRCUserStatus>>();
		for (Entry<IRCUser, Set<IRCUserStatus>> _entry : channel.getUsers().entrySet())
		{
			_map.put(new UnmodifiableIRCUser(_entry.getKey()), Collections.unmodifiableSet(_entry.getValue()));
		}
		
		return Collections.unmodifiableMap(_map);
	}

	@Override
	public boolean equals(Object aObject)
	{
		if (aObject != null)
		{
			if (aObject instanceof IRCChannel)
			{
				return ((IRCChannel)aObject).getName().equals(channel.getName());
			}
			else if (aObject instanceof String)
			{
				return aObject.equals(channel.getName());
			}
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		return channel.hashCode();
	}
}
