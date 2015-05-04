package com.ircclouds.irc.api.ctcp;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

import org.slf4j.*;

public class DCCSender
{
	private static final Logger LOG = LoggerFactory.getLogger(DCCSender.class);
	private static final int READ_BUFFER_SIZE = 1024;
	
	private Integer timeout;
	private Integer listeningPort;
	private Integer resumePos;

	private DCCSendCallback callback;
	private int totalBytesTransferred;
	private int totalAcksRead;
	
	private Exception readerExc;
	private Exception writerExc;
	
	public DCCSender(Integer aPort, Integer aTimeout, DCCSendCallback aCallback)
	{
		this(aTimeout, aPort, 0, aCallback);
	}

	public DCCSender(int aTimeout, Integer aPort, Integer aResumePosition, DCCSendCallback aCallback)
	{
		timeout = aTimeout;
		listeningPort = aPort;
		resumePos = aResumePosition;
		callback = aCallback;
	}

	public void setResumePosition(int aResumePosition)
	{
		resumePos = aResumePosition;
	}
	
	public void send(final File aFile)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				ServerSocketChannel _ssc = null;
				SocketChannel _sc = null;

				long _timeBefore = 0;
				
				try
				{
					_timeBefore = System.currentTimeMillis();

					_ssc = ServerSocketChannel.open();
					_ssc.configureBlocking(false);
					_ssc.socket().bind(new InetSocketAddress(listeningPort));

					Selector _selector = Selector.open();
					_ssc.register(_selector, SelectionKey.OP_ACCEPT);

					if (_selector.select(timeout) > 0 && _selector.selectedKeys().iterator().next().isAcceptable())
					{
						_sc = _ssc.accept();

						Thread _ar = getACKsReader(_sc);
						_ar.start();

						if (_sc != null)
						{
							writeFileToChannel(aFile, _sc);
						}

						_ar.join();
					}
				}
				catch (Exception aExc)
				{
					LOG.error("Error Transmitting File", aExc);
					
					writerExc = aExc;
				}
				finally
				{
					if (_ssc != null)
						close(_ssc);
					if (_sc != null)
						close(_sc);
										
					callBack(aFile, System.currentTimeMillis() - _timeBefore);
				}
			}
		}, "DCCSender").start();
	}

	private void callBack(File aFile, final long aTotalTime)
	{
		DCCSendResult _dccSendRes = new DCCSendResult()
		{				
			@Override
			public int totalBytesSent()
			{
				return totalBytesTransferred;
			}
			
			@Override
			public int getNumberOfAcksReceived()
			{
				return totalAcksRead;
			}

			@Override
			public long totalTime()
			{
				return aTotalTime;
			}
			
			@Override
			public String toString()
			{
				return "Total bytes sent: " + totalBytesTransferred + " - Number of acks received: " + totalAcksRead + " - Total time: " + aTotalTime;
			}
		};
		
		if (totalBytesTransferred == aFile.length())
		{
			LOG.debug(_dccSendRes.toString());
			
			callback.onSuccess(_dccSendRes);
		}
		else
		{
			DCCSendException _dccSendExc = new DCCSendException(_dccSendRes, readerExc, writerExc);

			LOG.debug("", _dccSendExc);
			
			callback.onFailure(_dccSendExc);
		}
	}
	
	private void writeFileToChannel(File aFile, SocketChannel aSocketChannel) throws IOException
	{
		FileInputStream _fis = new FileInputStream(aFile);
		FileChannel _fc = _fis.getChannel();

		long _size = aFile.length();
		long _position = resumePos;

		while (_position < _size)
		{
			_position += _fc.transferTo(_position, _size - _position, aSocketChannel);
		}

		if (_fis != null)
			close(_fis);
		if (_fc != null)
			close(_fc);
	}

	private Thread getACKsReader(final SocketChannel aSocketChannel)
	{
		return new Thread(new Runnable()
		{
			public void run()
			{
				ByteBuffer _bb = ByteBuffer.allocate(READ_BUFFER_SIZE).order(ByteOrder.BIG_ENDIAN);
				
				boolean _hasReadData = false;
				boolean _cleared = false;
				try
				{
					ProgressReader _pr = getProgressReader();
					
					int _readCount = 0;
					totalAcksRead = 0;
					
					while ((_readCount = aSocketChannel.read(_bb)) > 0)
					{
						totalAcksRead += _readCount / 4;
						_pr.read(_bb, _readCount);
						
						_hasReadData = true;
						_cleared = false;

						if (!_bb.hasRemaining())
						{
							_bb.clear();
							_cleared = true;
						}
					}

					if (_hasReadData)
					{
						if (!_cleared)
						{
							_bb.flip();
							if (_bb.limit() >= 4)
								_bb.position(_bb.limit() - 4);
						}
						else
						{
							_bb.position(READ_BUFFER_SIZE - 4);
						}

						totalBytesTransferred = _bb.getInt();
					}
				}
				catch (IOException aExc)
				{
					LOG.error("Error Reading Acks", aExc);
					readerExc = aExc;
				}
			}

		}, "DCCACKsReader");
	}


	private ProgressReader getProgressReader()
	{
		ProgressReader _pr = null;
		
		if (callback instanceof DCCSendProgressCallback)
		{
			_pr = new ProgressReaderImpl((DCCSendProgressCallback) callback);
		}
		else
		{
			_pr = new NullProgressReader();
		}
		
		return _pr;
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
	
	interface ProgressReader
	{
		void read(ByteBuffer aBuffer, int aReadCount);
	}
	
	class NullProgressReader implements ProgressReader
	{
		@Override
		public void read(ByteBuffer aBuffer, int aReadCount)
		{
			// Do Nothing
		}		
	}
	
	class ProgressReaderImpl implements ProgressReader
	{
		DCCSendProgressCallback callback;
		
		ProgressReaderImpl(DCCSendProgressCallback aCallback)
		{
			callback = aCallback;
		}
		
		@Override
		public void read(ByteBuffer aByteBuffer, int aReadCount)
		{
			aByteBuffer.position(aByteBuffer.position() - aReadCount);
			for (int _i = 0; _i < aReadCount / 4; _i++)
			{
				callback.onProgress(aByteBuffer.getInt());
			}
		}
	}	
}