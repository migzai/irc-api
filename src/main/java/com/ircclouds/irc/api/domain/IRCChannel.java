package com.ircclouds.irc.api.domain;

import java.util.*;

import com.ircclouds.irc.api.utils.*;

public class IRCChannel
{
	private WritableIRCChannel channel;

	private IRCTopic utopic;
	private Set<ChannelMode> umodes;
	private Set<IRCUser> uusers;

	public IRCChannel(WritableIRCChannel aChannel)
	{
		channel = aChannel;
		utopic = new IRCTopic(channel.getTopic());
		umodes = Collections.unmodifiableSet(channel.getModes());
		uusers = new UnmodifiableSetDelegate<WritableIRCUser, IRCUser>(channel.getUsers())
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

	public Set<IRCUser> getUsers()
	{
		return uusers;
	}

	public boolean containsUser(IRCUser aUser)
	{
		return uusers.contains(new WritableIRCUser(aUser.getNick()));
	}
	
	public boolean containsUser(String aNickname)
	{
		return uusers.contains(new WritableIRCUser(aNickname));
	}

	public Set<IRCUserStatus> getStatusesForUser(String aNickname)
	{
		return channel.getStatusesForUser(new WritableIRCUser(aNickname));
	}
	
	public Set<IRCUserStatus> getStatusesForUser(IRCUser aUser)
	{
		return channel.getStatusesForUser(new WritableIRCUser(aUser.getNick()));
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
