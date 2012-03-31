package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.messages.interfaces.*;

/**
 * 
 * @author
 * 
 */
public class ServerMessage implements IMessage, IHasText, IHasNumericCode
{
	private int numericCode;
	private String text;

	public ServerMessage(Integer aNumericCode, String aText)
	{
		numericCode = aNumericCode;
		text = aText;
	}
	
	public String getText()
	{
		return text;
	}

	public Integer getNumericCode()
	{
		return numericCode;
	}
}
