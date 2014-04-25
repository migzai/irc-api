package com.ircclouds.irc.api;

import java.io.*;

import com.ircclouds.irc.api.domain.messages.interfaces.*;

public interface IMessageReader
{
	boolean available() throws IOException;

	IMessage readMessage();
	
	void reset();
}
