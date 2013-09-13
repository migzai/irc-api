package com.ircclouds.irc.api.comms;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

public class SocketChannelConnection implements IConnection
{
	private SocketChannel channel;
	private ByteBuffer buffer = ByteBuffer.allocate(2048);

	@Override
	public int write(String aMessage) throws IOException
	{
		return channel.write(ByteBuffer.wrap(aMessage.getBytes()));
	}

	@Override
	public boolean open(String aHostname, int aPort) throws IOException
	{
		if (channel == null || !channel.isConnected())
		{
			return (channel = SocketChannel.open()).connect(new InetSocketAddress(aHostname, aPort));
		}
		else
		{
			throw new RuntimeException("Socket is already open.");
		}
	}

	@Override
	public String read() throws IOException
	{
		buffer.clear();
		if (channel.read(buffer) == -1)
		{
			throw new EndOfStreamException();
		}
		buffer.flip();

		byte[] _bytes = new byte[buffer.limit()];
		buffer.get(_bytes);
		return new String(_bytes);
	}

	@Override
	public void close() throws IOException
	{
		if (channel != null && channel.isOpen())
		{
			channel.close();
		}
	}
}
