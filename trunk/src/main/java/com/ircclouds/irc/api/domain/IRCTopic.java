package com.ircclouds.irc.api.domain;

import java.util.*;

public class IRCTopic
{	
	private WritableIRCTopic topic;
	
	public IRCTopic(WritableIRCTopic aTopic)
	{
		topic = aTopic;
	}
	
	public String getValue()
	{
		return topic.getValue();
	}

	public String getSetBy()
	{
		return topic.getSetBy();
	}
	
	public Date getDate()
	{
		return topic.getDate();
	}
}
