package com.ircclouds.irc.api;

import java.io.*;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.filters.*;
import com.ircclouds.irc.api.listeners.*;

public interface IIRCSession
{
	ICommandServer getCommandServer();

	void addListeners(IMessageListener... aListener);
	
	void removeListener(IMessageListener aListener);
	
	boolean open(IRCServer aServer) throws IOException;
	
	void close() throws IOException;

	IMessageFilter getMessageFilter();
}
