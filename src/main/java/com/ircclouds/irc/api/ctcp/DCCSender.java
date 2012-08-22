package com.ircclouds.irc.api.ctcp;

import java.io.*;
import java.net.*;
import java.nio.*;
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
			_ssc.configureBlocking(false);
			_ssc.socket().bind(new InetSocketAddress(listeningPort));

			Selector _selector = Selector.open();

			_ssc.register(_selector, SelectionKey.OP_ACCEPT);
			if (_selector.select(timeout) > 0)
			{
				SelectionKey _sKey = _selector.selectedKeys().iterator().next();
				if (_sKey.isAcceptable())
				{
					_sc = _ssc.accept();
					if (_sc != null)
					{
						_fis = new FileInputStream(file);
						_fc = _fis.getChannel();

						long _size = file.length();
						long _position = resumePos;

						while (_position < _size - resumePos)
						{
							_position += _fc.transferTo(_position, _size - _position, _sc);
						}

						ByteBuffer _bb = ByteBuffer.allocate(1024).order(ByteOrder.BIG_ENDIAN);

						boolean _readData = false;
						boolean _cleared = false;
						while (_sc.read(_bb) > 0)
						{
							_readData = true;
							_cleared = false;

							if (!_bb.hasRemaining())
							{
								_bb.clear();
								_cleared = true;
							}
						}

						if (_readData)
						{
							if (!_cleared)
							{
								_bb.flip();
								if (_bb.limit() >= 4)
									_bb.position(_bb.limit() - 4);
							}
							else
							{
								_bb.position(1020);
							}

							System.out.println(_bb.getInt());
						}
					}
				}
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
			if (_sc != null)
				close(_sc);
			if (_fis != null)
				close(_fis);
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
