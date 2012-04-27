package com.ircclouds.irc.api.commands;

public class SendPrivateMessage implements ICommand
{
	private static final String PRIVMSG = "PRIVMSG ";

	private String nick;
	private String msg;
	private Integer asyncRandConstant;
	
	public SendPrivateMessage(String aNick, String aText)
	{
		this(aNick, aText, null);
	}

	public SendPrivateMessage(String aNick, String aText, Integer aAsyncRandConstant)
	{
		nick = aNick;
		msg = aText;
		asyncRandConstant = aAsyncRandConstant;
	}
	
	@Override
	public String asString()
	{
		if (asyncRandConstant == null)
		{
			return PRIVMSG + nick + " :" + msg;
		}
		else
		{
			return PRIVMSG + nick + "," + asyncRandConstant + " :" + msg;
		}
	}
}