package com.ircclouds.irc.api.listeners;

import java.io.*;
import java.net.*;
import java.nio.channels.*;

import com.ircclouds.irc.api.*;

public class DCCSendListener implements Runnable
{
	private File file;
	private Integer timeout;
	private Integer listeningPort;

	public DCCSendListener(File aFile, Integer aTimeout, Integer aPort)
	{
		file = aFile;
		timeout = aTimeout;
		listeningPort = aPort;
	}	
	
	@Override
	public void run()
	{
		ServerSocketChannel _ssc = null;
		SocketChannel _sc = null;
		FileChannel _fc = null;
		try
		{
			_ssc = ServerSocketChannel.open();
			_ssc.socket().bind(new InetSocketAddress(listeningPort));
			_ssc.socket().setSoTimeout(timeout);
			_sc = _ssc.accept();

			_fc = new FileInputStream(file).getChannel();
			_fc.transferTo(0, file.length(), _sc);
		}
		catch (IOException aExc)
		{
			throw new ApiException(aExc);
		}
		finally
		{
			if (_ssc != null)
				close(_ssc);
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
