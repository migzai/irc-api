package com.ircclouds.irc.api.comms;

import java.io.*;
import java.net.*;

import javax.net.ssl.*;

public interface IConnection
{
	boolean open(String aHostname, int aPort, SSLContext aContext, Proxy aProxy, boolean resolveByProxy) throws IOException;
	
	void close() throws IOException;

	int write(String aMessage) throws IOException;

	String read() throws IOException;
		
	public class EndOfStreamException extends IOException
	{				
	}
}
