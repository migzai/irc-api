package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class ErrorMessage implements IMessage, IHasText
{	
	private String text;
	
	public ErrorMessage(String aText)
	{
		text = aText;
	}

	@Override
	public String getText()
	{
		return text;
	}
}
