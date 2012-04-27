package com.ircclouds.irc.api.commands;

public class SendChannelActionMessage implements ICommand
{
	private static final char NUL = '\001';
	private static final String PRIVMSG = "PRIVMSG ";
	private static final String ACTION = "ACTION ";

	private String channel;
	private String msg;
	private Integer asyncRandConstant;
	
	public SendChannelActionMessage(String aChannel, String aText)
	{
		this(aChannel, aText, null);
	}

	public SendChannelActionMessage(String aChannel, String aText, Integer aAsyncRandConstant)
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
			return PRIVMSG + channel + " :" + NUL + ACTION + msg + NUL;
		}
		else
		{
			return PRIVMSG + channel + "," + asyncRandConstant + " :" + NUL + ACTION + msg + NUL;
		}
	}
}