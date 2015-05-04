package com.ircclouds.irc.api.ctcp;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import nl.dannyvanheumen.nio.ProxiedSocketChannel;

import org.slf4j.*;

public class DCCReceiver
{
	private static final Logger LOG = LoggerFactory.getLogger(DCCReceiver.class);

	private final ByteBuffer bb = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);

	private final DCCReceiveCallback callback;

	private final Proxy proxy;

	private int totalBytesReceived;
	private int totalAcksSent;

	private IOException exc;

	public DCCReceiver(DCCReceiveCallback aCallback, Proxy aProxy)
	{
		proxy = aProxy;
		callback = aCallback;
	}
	
	public void receive(final File aFile, final Integer aResumePos, final Integer aSize, final SocketAddress aAddress)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				SocketChannel _sc = null;
				FileChannel _fc = null;
				FileOutputStream _fos = null;

				long _timeBefore = 0;

				try
				{
					_timeBefore = System.currentTimeMillis();
					if (proxy == null)
					{
						_sc = SocketChannel.open(aAddress);
					}
					else
					{
						_sc = new ProxiedSocketChannel(proxy);
						_sc.connect(aAddress);
					}
					_fos = new FileOutputStream(aFile);
					_fc = _fos.getChannel();

					long _read = aResumePos;
					while (_read < aSize)
					{
						_read += _fc.transferFrom(_sc, aResumePos + _read, aSize);
						writeTotalBytesReceived(_sc, (int) _read);
					}
				}
				catch (IOException aExc)
				{
					exc = aExc;
					LOG.error("", aExc);
				}
				finally
				{
					if (_sc != null)
						close(_sc);
					if (_fos != null)
						close(_fos);
					if (_fc != null)
						close(_fc);
					
					callBack(aSize, System.currentTimeMillis() - _timeBefore);
				}
			}
		}).start();
	}

	private void callBack(int aPromisedFileSize, final long aTimeTaken)
	{
		DCCReceiveResult _dccRecRes = new DCCReceiveResult()
		{
			@Override
			public long totalTime()
			{
				return aTimeTaken;
			}
			
			@Override
			public int totalBytesReceived()
			{
				return totalBytesReceived;
			}
			
			@Override
			public int getNumberOfAcksSent()
			{
				return totalAcksSent;
			}
		};
		
		if (totalBytesReceived == aPromisedFileSize)
		{
			callback.onSuccess(_dccRecRes);
		}
		else
		{
			callback.onFailure(new DCCReceiveException(_dccRecRes, exc));
		}
	}

	private void writeTotalBytesReceived(SocketChannel aSocketChannel, int aCount) throws IOException
	{
		bb.clear();
		bb.putInt(aCount);
		
		aSocketChannel.write(bb);
		if (callback instanceof DCCReceiveProgressCallback)
		{
			((DCCReceiveProgressCallback) callback).onProgress(aCount);
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
			LOG.error("", aExc);
		}
	}
}
