package com.ircclouds.irc.api.domain;

import java.util.*;

import com.ircclouds.irc.api.utils.*;

public class IRCChannel
{
	private WritableIRCChannel channel;

	private IRCTopic utopic;
	private Set<ChannelMode> umodes;
	private List<IRCUser> uusers;

	public IRCChannel(WritableIRCChannel aChannel)
	{
		channel = aChannel;
		utopic = new IRCTopic(channel.getTopic());
		umodes = Collections.unmodifiableSet(channel.getModes());
		uusers = new UnmodifiableListDelegate<WritableIRCUser, IRCUser>(channel.getUsers())
		{
			@Override
			protected IRCUser newInstance(WritableIRCUser aT)
			{
				return new IRCUser(aT);
			}
		};
	}

	public String getName()
	{
		return channel.getName();
	}

	public IRCTopic getTopic()
	{
		return utopic;
	}

	public Set<ChannelMode> getModes()
	{
		return umodes;
	}

	public List<IRCUser> getUsers()
	{
		return uusers;
	}
	
	public boolean containsUser(String aNickname)
	{
		return uusers.contains(new WritableIRCUser(aNickname));
	}

	public Set<IRCUserStatus> getStatusesForUser(String aNickname)
	{
		return channel.getStatusesForUser(new WritableIRCUser(aNickname));
	}

	@Override
	public boolean equals(Object aObject)
	{
		if (aObject != null)
		{
			if (aObject instanceof WritableIRCChannel)
			{
				return ((WritableIRCChannel) aObject).getName().equals(channel.getName());
			}
			else if (aObject instanceof IRCChannel)
			{
				return ((IRCChannel) aObject).getName().equals(channel.getName());
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
