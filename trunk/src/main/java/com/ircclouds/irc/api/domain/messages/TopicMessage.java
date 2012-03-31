package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class TopicMessage implements IChannelMessage
{
	private String channel;
	private IRCTopic topic;

	public TopicMessage(String aChannel, IRCTopic aTopic)
	{
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
	public IRCUser getFromUser()
	{
		return null;
	}
}
