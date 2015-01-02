package com.ircclouds.irc.api;

import java.io.*;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.filters.*;
import com.ircclouds.irc.api.listeners.*;
import com.ircclouds.irc.api.state.*;

public interface IIRCSession
{
	ICommandServer getCommandServer();

	void addListeners(MESSAGE_VISIBILITY aLevel, IMessageListener... aListener);
	
	void removeListener(IMessageListener aListener);
	
	boolean open(IRCServer aServer, Callback<IIRCState> aCallback) throws IOException;
	
	void close() throws IOException;

	IMessageFilter getMessageFilter();

	void dispatchClientError(Exception e);
}
