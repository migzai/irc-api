package com.ircclouds.irc.api.domain.messages.interfaces;

import com.ircclouds.irc.api.domain.*;

public interface IUserMessage extends IMessage
{
	@Override
	public IRCUser getSource();
}
