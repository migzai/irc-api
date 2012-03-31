package com.ircclouds.irc.api.commands;

/**
 * 
 * @author didry
 * 
 */
public class ChangeTopicCmd implements ICommand
{
	private static final String TOPIC_KEY = "TOPIC";

	private String channel;
	private String topic;

	public ChangeTopicCmd(String aChannel, String aTopic)
	{
		channel = aChannel;
		topic = aTopic;
	}

	@Override
	public String asString()
	{
		return new StringBuffer().append(TOPIC_KEY).append(" ").append(channel).append(" :").append(topic).toString();
	}

}
