package com.ircclouds.irc.api;

import java.util.*;

import com.ircclouds.irc.api.domain.messages.interfaces.*;

public interface IMessagesReader
{
	boolean available();

	List<IMessage> readMessages();
	
	void reset();
}
