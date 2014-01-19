package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class ServerPing implements IHasText, IServerMessage
{
	private String text;

	public String getText()
	{
		return text;
	}

	public void setText(String aText)
	{
		text = aText;
	}

	@Override
	public IRCServer getSource()
	{
		return null;
	}

	@Override
	public String asRaw()
	{
		return new StringBuffer().append("PING :").append(text).toString();
	}
}
