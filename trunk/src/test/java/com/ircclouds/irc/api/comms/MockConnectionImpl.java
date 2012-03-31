package com.ircclouds.irc.api.comms;

import java.io.*;
import java.nio.*;

import mockit.*;

public class MockConnectionImpl implements IConnection
{
	private BufferedReader reader;
	private String filename;
	private CharBuffer buffer = CharBuffer.allocate(2048);

	public MockConnectionImpl(String aFileName)
	{
		filename = aFileName;
	}
	
	@Mock
	public boolean open(String aHostname, int aPort) throws IOException
	{
		InputStream _resourceAsStream = MockConnectionImpl.class.getResourceAsStream(filename);
		if (_resourceAsStream != null)
		{
			return (reader = new BufferedReader(new InputStreamReader(_resourceAsStream))).ready();
		}
		
		throw new RuntimeException("Error locating resource.");
	}

	@Mock
	public void close() throws IOException
	{
		if (reader != null)
		{
			reader.close();			
		}
	}

	@Mock
	public int write(String aMessage) throws IOException
	{
		return aMessage.length();
	}

	@Mock
	public String read() throws IOException
	{
		buffer.clear();
		reader.read(buffer);
		buffer.flip();
		return buffer.toString();
	}
}
