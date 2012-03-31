package com.ircclouds.irc.api.om;

import com.ircclouds.irc.api.domain.messages.*;

public class ServerAuthBuilder implements IBuilder<ServerAuth>
{
	@Override
	public ServerAuth build(String aMessage)
	{
		return new ServerAuth(aMessage);
	}
}
