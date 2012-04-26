package com.ircclouds.irc.api.commands;

public class SendChannelMessage implements ICommand
{
	private static final String PRIVMSG = "PRIVMSG ";

	private String channel;
	private String msg;
	private Integer asyncRandConstant;

	public SendChannelMessage(String aChannel, String aText)
	{
		this(aChannel, aText, null);
	}
	
	public SendChannelMessage(String aChannel, String aText, Integer aAsyncRandConstant)
	{
		channel = aChannel;
		msg = aText;
		asyncRandConstant = aAsyncRandConstant;
	}

	@Override
	public String asString()
	{
		if (asyncRandConstant == null)
		{
			return PRIVMSG + channel + " :" + msg;
		}
		else
		{
			return PRIVMSG + channel + "," + asyncRandConstant + " :" + msg;
		}
	}
}
