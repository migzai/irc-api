package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class TopicMessage implements IChannelMessage, IUserMessage
{
	private String channel;
	private WritableIRCTopic topic;

	public TopicMessage(String aChannel, WritableIRCTopic aTopic)
	{
		channel = aChannel;
		topic = aTopic;
	}

	public String getChannelName()
	{
		return channel;
	}

	public WritableIRCTopic getTopic()
	{
		return topic;
	}

	@Override
	public WritableIRCUser getSource()
	{
		return null;
	}
}
