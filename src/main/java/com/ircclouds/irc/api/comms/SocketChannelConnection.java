package com.ircclouds.irc.api.comms;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

import javax.net.ssl.*;

import nl.dannyvanheumen.nio.*;

public class SocketChannelConnection implements IConnection
{
	private SocketChannel channel;
	private final ByteBuffer buffer = ByteBuffer.allocate(2048);

	@Override
	public int write(String aMessage) throws IOException
	{
		return channel.write(ByteBuffer.wrap(aMessage.getBytes()));
	}

	@Override
	public boolean open(String aHostname, int aPort, SSLContext aCtx, Proxy aProxy) throws IOException
	{
		if (channel == null || !channel.isConnected())
		{
			if (aProxy != null && aProxy.type() == Proxy.Type.SOCKS)
			{
				channel = new ProxiedSocketChannel(aProxy);
			}
			else
			{
				channel = SocketChannel.open();
			}
			return channel.connect(new InetSocketAddress(aHostname, aPort));
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
