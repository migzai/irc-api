package com.ircclouds.irc.api.om;

import com.ircclouds.irc.api.domain.messages.interfaces.*;

public interface IBuilder<T extends IMessage>
{
	T build(String aMessage);
}
