package com.ircclouds.irc.api.commands;


public class SendPrivateMessage implements ICommand
{

	private static final String PRIVMSG = "PRIVMSG ";

	private String nick;
	private String msg;

	public SendPrivateMessage(String aNick, String aText)
	{
		nick = aNick;
		msg = aText;
	}

	@Override
	public String asString()
	{
		return PRIVMSG + nick + " :" + msg;
	}

}
