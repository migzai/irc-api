package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.messages.interfaces.*;

public abstract class AbstractNotice implements IHasText
{
	String text;

	public AbstractNotice(String aText)
	{
		text = aText;
	}
	
	public String getText()
	{
		return text;
	}
}
