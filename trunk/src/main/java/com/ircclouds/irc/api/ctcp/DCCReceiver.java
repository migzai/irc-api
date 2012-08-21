package com.ircclouds.irc.api.ctcp;

import java.io.*;
import java.net.*;
import java.nio.channels.*;

import com.ircclouds.irc.api.*;

public class DCCReceiver implements Runnable
{
	private SocketAddress address;
	private Integer resumePos;
	private Integer size;
	private File file;
	
	public DCCReceiver(File aFile, Integer aResumePos, Integer aSize, SocketAddress aAddress)
	{
		file = aFile;
		resumePos = aResumePos;
		size = aSize;
		address = aAddress;
	}
	
	@Override
	public void run()
	{
		SocketChannel _sc = null;
		FileChannel _fc = null;
		
		try
		{
			_sc = SocketChannel.open(address);
			_fc = new FileOutputStream(file).getChannel();
			_fc.transferFrom(_sc, resumePos, size);
		}
		catch (IOException aExc)
		{
			throw new ApiException(aExc);
		}
		finally
		{
			if (_sc != null)
				close(_sc);
			if (_fc != null)
				close(_fc);
		}
	}

	private void close(Closeable aCloseable)
	{
		try
		{
			aCloseable.close();
		}
		catch (IOException aExc)
		{
		}
	}
}
