package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class ErrorMessage implements IServerMessage, IHasText
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

	@Override
	public IRCServer getSource()
	{
		return null;
	}
}
