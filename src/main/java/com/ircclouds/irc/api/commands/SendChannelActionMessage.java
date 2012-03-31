package com.ircclouds.irc.api.commands;


public class SendChannelActionMessage implements ICommand
{

	private static final char NUL = '\001';
	private static final String PRIVMSG = "PRIVMSG ";
	private static final String ACTION = "ACTION ";

	private String channel;
	private String msg;

	public SendChannelActionMessage(String aChannel, String aText)
	{
		channel = aChannel;
		msg = aText;
	}

	@Override
	public String asString()
	{
		return PRIVMSG + channel + " :" + NUL + ACTION + msg + NUL;
	}

}