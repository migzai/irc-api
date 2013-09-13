package com.ircclouds.irc.api;

import com.ircclouds.irc.api.domain.messages.interfaces.*;

public interface IMessageReader
{
	boolean available();

	IMessage readMessage();
	
	void reset();
}
