package com.ircclouds.irc.api.domain.messages.interfaces;

import com.ircclouds.irc.api.domain.*;

public interface IServerMessage extends IMessage
{
	@Override
	public IRCServer getSource();
}
