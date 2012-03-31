package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.messages.interfaces.*;

/**
 * 
 * @author
 * 
 */
public abstract class AbstractNotice implements IHasText, IMessage
{
	private String text;

	public AbstractNotice(String aText)
	{
		text = aText;
	}
	
	public String getText()
	{
		return text;
	}
}
