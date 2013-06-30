package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

/**
 * 
 * @author
 * 
 */
public class ServerNumericMessage implements IServerMessage, IHasText, IHasNumericCode
{
	private int numericCode;
	private String text;
	private IRCServer server;

	public ServerNumericMessage(Integer aNumericCode, String aText, IRCServer aServer)
	{
		numericCode = aNumericCode;
		text = aText;
		server = aServer;
	}
	
	public String getText()
	{
		return text;
	}

	public Integer getNumericCode()
	{
		return numericCode;
	}

	@Override
	public IRCServer getSource()
	{
		return server;
	}
}
