package com.ircclouds.irc.api.comms;

import java.io.*;

public interface IConnection
{
	boolean open(String aHostname, int aPort) throws IOException;
	
	void close() throws IOException;

	int write(String aMessage) throws IOException;

	String read() throws IOException;
}
