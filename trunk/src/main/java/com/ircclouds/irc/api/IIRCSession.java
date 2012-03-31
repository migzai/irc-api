package com.ircclouds.irc.api;

import java.io.*;

import com.ircclouds.irc.api.commands.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.listeners.*;

public interface IIRCSession
{
	void execute(ICommand aCommand) throws IOException;

	void addListeners(IMessageListener... aListener);
	
	void removeListener(IMessageListener aListener);
	
	boolean open(IRCServer aServer) throws IOException;
	
	void close() throws IOException;
}
