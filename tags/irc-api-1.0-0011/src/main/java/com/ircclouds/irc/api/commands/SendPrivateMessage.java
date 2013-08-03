package com.ircclouds.irc.api.commands;

public class SendPrivateMessage implements ICommand
{
	private static final String PRIVMSG = "PRIVMSG ";

	private String target;
	private String msg;
	private Integer asyncRandConstant;
	
	public SendPrivateMessage(String aChannel, String aText)
	{
		this(aChannel, aText, null);
	}

	public SendPrivateMessage(String aTarget, String aText, Integer aAsyncRandConstant)
	{
		target = aTarget;
		msg = aText;
		asyncRandConstant = aAsyncRandConstant;
	}
	
	@Override
	public String asString()
	{
		if (asyncRandConstant == null)
		{
			return PRIVMSG + target + " :" + msg;
		}
		else
		{
			return PRIVMSG + target + "," + asyncRandConstant + " :" + msg;
		}
	}
}