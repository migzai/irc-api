package com.ircclouds.irc.api.listeners;

import com.ircclouds.irc.api.domain.messages.interfaces.*;

public interface IMessageListener
{
	void onMessage(IMessage aMessage);
}
