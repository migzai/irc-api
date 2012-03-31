package com.ircclouds.irc.api.commands;


public class SendChannelMessage implements ICommand
{

	private static final String PRIVMSG = "PRIVMSG ";

	private String channel;
	private String msg;

	public SendChannelMessage(String aChannel, String aText)
	{
		channel = aChannel;
		msg = aText;
	}

	@Override
	public String asString()
	{
		return PRIVMSG + channel + " :" + msg;
	}

}
