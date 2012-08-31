package com.ircclouds.irc.api.commands;

public class SendNoticeMessage implements ICommand
{
	private static final String NOTICE = "NOTICE ";

	private String channel;
	private String msg;
	private Integer asyncRandConstant;

	public SendNoticeMessage(String aChannel, String aText)
	{
		this(aChannel, aText, null);
	}
	
	public SendNoticeMessage(String aChannel, String aText, Integer aAsyncRandConstant)
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
			return NOTICE + channel + " :" + msg;
		}
		else
		{
			return NOTICE + channel + "," + asyncRandConstant + " :" + msg;
		}
	}
}