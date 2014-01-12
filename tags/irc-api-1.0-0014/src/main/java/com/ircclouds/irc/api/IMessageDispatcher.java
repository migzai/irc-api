package com.ircclouds.irc.api;

import com.ircclouds.irc.api.domain.messages.interfaces.*;
import com.ircclouds.irc.api.filters.*;
import com.ircclouds.irc.api.listeners.*;

public interface IMessageDispatcher
{
	void dispatch(IMessage aMessage, TargetListeners aTargetListeners);

	void dispatchToPrivateListeners(IMessage aMessage);
	
	void register(IMessageListener aListener, MESSAGE_VISIBILITY aVisibility);

	void unregister(IMessageListener aListener);
}
