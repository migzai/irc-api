package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class TopicMessage implements IChannelMessage, IUserMessage
{
	private String channel;
	private IRCTopic topic;
	private IRCUser user;

	public TopicMessage(IRCUser aUser, String aChannel, IRCTopic aTopic)
	{
		user = aUser;
		channel = aChannel;
		topic = aTopic;
	}

	public String getChannelName()
	{
		return channel;
	}

	public IRCTopic getTopic()
	{
		return topic;
	}

	@Override
	public IRCUser getSource()
	{
		return user;
	}

	@Override
	public String asRaw()
	{
		return new StringBuffer().append(":").append(user).append(" TOPIC ").append(channel).append(" :").append(topic.getValue()).toString();
	}
}
