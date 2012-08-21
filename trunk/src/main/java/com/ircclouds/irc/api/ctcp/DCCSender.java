package com.ircclouds.irc.api.ctcp;

import java.io.*;
import java.net.*;
import java.nio.channels.*;

import com.ircclouds.irc.api.*;

public class DCCSender implements Runnable
{
	private File file;
	private Integer timeout;
	private Integer listeningPort;
	private Integer resumePos;
	
	public DCCSender(File aFile, Integer aPort, Integer aTimeout)
	{
		this(aFile, aTimeout, aPort, 0);
	}	
	
	public DCCSender(File aFile, int aTimeout, Integer aPort, Integer aResumePosition) 
	{
		file = aFile;
		timeout = aTimeout;
		listeningPort = aPort;
		resumePos = aResumePosition;
	}

	@Override
	public void run()
	{
		ServerSocketChannel _ssc = null;
		SocketChannel _sc = null;
		FileChannel _fc = null;
		FileInputStream _fis = null;
		
		try
		{
			_ssc = ServerSocketChannel.open();
			_ssc.socket().bind(new InetSocketAddress(listeningPort));
			_ssc.socket().setSoTimeout(timeout);
			_sc = _ssc.accept();

			_fis = new FileInputStream(file);
			_fc = _fis.getChannel();
			
	        long _size = file.length();
	        
	        long _position = resumePos;
	        while (_position < _size - resumePos) 
	        {
	            _position += _fc.transferTo(_position, _size, _sc);
	        }
		}
		catch (IOException aExc)
		{
			throw new ApiException(aExc);
		}
		finally
		{
			if (_ssc != null)
				close(_ssc);
			if (_fis != null)
				close(_fis);
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
