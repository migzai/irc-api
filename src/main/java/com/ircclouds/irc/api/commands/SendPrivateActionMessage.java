package com.ircclouds.irc.api.commands;

public class SendPrivateActionMessage implements ICommand
{	
	private static final char NUL = '\001';
	private static final String PRIVMSG = "PRIVMSG ";
	private static final String ACTION = "ACTION ";

	private String nick;
	private String msg;
	private Integer asyncRandConstant;

	public SendPrivateActionMessage(String aNick, String aText)
	{
		this(aNick, aText, null);
	}

	public SendPrivateActionMessage(String aNick, String aText, Integer aAsyncRandConstant)
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
			return PRIVMSG + nick + " :" + NUL + ACTION + msg + NUL;
		}
		else
		{
			return PRIVMSG + nick + "," + asyncRandConstant + " :" + NUL + ACTION + msg + NUL;
		}
	}
}
