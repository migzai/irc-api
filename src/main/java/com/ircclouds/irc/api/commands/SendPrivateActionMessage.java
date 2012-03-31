package com.ircclouds.irc.api.commands;


public class SendPrivateActionMessage implements ICommand
{
	
	private static final char NUL = '\001';
	private static final String PRIVMSG = "PRIVMSG ";
	private static final String ACTION = "ACTION ";

	private String nick;
	private String msg;

	public SendPrivateActionMessage(String aNick, String aText)
	{
		nick = aNick;
		msg = aText;
	}

	@Override
	public String asString()
	{
		return PRIVMSG + nick + " :" + NUL + ACTION + msg + NUL;
	}

}
