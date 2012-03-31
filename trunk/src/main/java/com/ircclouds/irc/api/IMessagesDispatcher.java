package com.ircclouds.irc.api;

import com.ircclouds.irc.api.domain.messages.interfaces.*;
import com.ircclouds.irc.api.listeners.*;

public interface IMessagesDispatcher
{
	void dispatch(IMessage aMessage);

	void register(IMessageListener aListener);

	void unregister(IMessageListener aListener);
}
