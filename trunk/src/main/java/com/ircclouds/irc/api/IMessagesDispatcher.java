package com.ircclouds.irc.api;

import com.ircclouds.irc.api.domain.messages.interfaces.*;
import com.ircclouds.irc.api.filters.*;
import com.ircclouds.irc.api.listeners.*;

public interface IMessagesDispatcher
{
	void dispatch(IMessage aMessage, TargetListeners aTargetListeners);

	void dispatchToPrivateListeners(IMessage aMessage);
	
	void register(IMessageListener aListener, ListenerLevel aLevel);

	void unregister(IMessageListener aListener);
}
