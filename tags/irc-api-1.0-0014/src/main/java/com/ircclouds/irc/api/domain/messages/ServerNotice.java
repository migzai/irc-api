package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class ServerNotice extends AbstractNotice implements IServerMessage
{
	private IRCServer server;
	
	public ServerNotice(String aText, IRCServer aServer)
	{
		super(aText);
		
		server = aServer;
	}

	@Override
	public IRCServer getSource()
	{
		return server;
	}
}
