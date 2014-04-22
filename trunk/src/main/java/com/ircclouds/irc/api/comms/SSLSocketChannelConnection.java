package com.ircclouds.irc.api.comms;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.security.*;

import javax.net.ssl.*;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;

import org.slf4j.*;

public class SSLSocketChannelConnection implements IConnection
{
	private static final Logger LOG = LoggerFactory.getLogger(SSLSocketChannelConnection.class);
		
	private SocketChannel sChannel;
	private SSLEngine sslEngine;

	private ByteBuffer appSendBuffer;
	private ByteBuffer appRecvBuffer;
	private ByteBuffer cipherSendBuffer;
	private ByteBuffer cipherRecvBuffer;

	private HandshakeStatus hStatus;
	private int remaingUnwraps;

	public boolean open(String aHostname, int aPort, SSLContext aContext) throws IOException
	{
		sslEngine  = aContext != null ? aContext.createSSLEngine(aHostname, aPort) : getDefaultSSLContext().createSSLEngine(aHostname, aPort);			
		sslEngine.setNeedClientAuth(false);
		sslEngine.setUseClientMode(true);
		sslEngine.beginHandshake();
		hStatus = sslEngine.getHandshakeStatus();

		appSendBuffer = ByteBuffer.allocate(sslEngine.getSession().getApplicationBufferSize());
		cipherSendBuffer = ByteBuffer.allocate(sslEngine.getSession().getPacketBufferSize());
		cipherRecvBuffer = ByteBuffer.allocate(sslEngine.getSession().getPacketBufferSize());
		appRecvBuffer = ByteBuffer.allocate(sslEngine.getSession().getApplicationBufferSize());
					
		return (sChannel = SocketChannel.open()).connect(new InetSocketAddress(aHostname, aPort));
	}

	public String read() throws IOException
	{
		doAnyPendingHandshake();

		if (!sslEngine.isInboundDone())
		{
			tryReadAndUnwrap();
		}
		else
		{
			throw new EndOfStreamException();
		}
		
		byte[] _bytes = new byte[appRecvBuffer.flip().limit()];
		appRecvBuffer.get(_bytes);
		appRecvBuffer.clear();
		
		return new String(_bytes);
	}

	public int write(String aMessage) throws IOException
	{
		doAnyPendingHandshake();

		appSendBuffer.clear();
		appSendBuffer.put(aMessage.getBytes()).flip();
		
		return wrapAndWrite();
	}

	public void close() throws IOException
	{
		try
		{
			if (!sslEngine.isOutboundDone())
			{
				sslEngine.closeOutbound();
				doAnyPendingHandshake();
			}
			else if (!sslEngine.isInboundDone())
			{
				sslEngine.closeInbound();
				processHandshake();
			}
		}
		finally
		{
			if (sChannel.isOpen())
			{
				sChannel.close();
			}
		}
	}

	private synchronized void doAnyPendingHandshake() throws IOException
	{
		while (processHandshake())
		{

		}
	}
	
	private synchronized boolean processHandshake() throws IOException
	{
		LOG.debug(Thread.currentThread().getName() + " " + hStatus);
		switch (hStatus)
		{
			case NEED_WRAP:
				wrapAndWrite();
				break;
			case NEED_UNWRAP:
				if (!sslEngine.isInboundDone())
				{
					tryReadAndUnwrap();
				}
				else
				{
					return false;
				}
				break;
			case NEED_TASK:
				executeTasks();
				break;
			case NOT_HANDSHAKING:
			case FINISHED:
				return false;
		}

		return true;
	}

	private void tryReadAndUnwrap() throws IOException, SSLException
	{
		if (remaingUnwraps == 0)
		{
			cipherRecvBuffer.clear();
			int _readCount = sChannel.read(cipherRecvBuffer);
			if (_readCount == -1)
			{
				throw new EndOfStreamException();					
			}
			remaingUnwraps += _readCount;
			LOG.debug("Reading: " + _readCount);
			cipherRecvBuffer.flip();
		}

		SSLEngineResult _hRes = sslEngine.unwrap(cipherRecvBuffer, appRecvBuffer);
		hStatus = _hRes.getHandshakeStatus();			
		remaingUnwraps -= _hRes.bytesConsumed();
		
		switch (_hRes.getStatus())
		{				
			case BUFFER_UNDERFLOW:
				int bytesRead = sChannel.read(cipherRecvBuffer.compact());
				if (bytesRead == -1)
				{
					throw new EndOfStreamException();
				}
				remaingUnwraps += bytesRead;
				cipherRecvBuffer.flip();					
				break;
			default:
				break;				
		}
	}

	private int wrapAndWrite() throws SSLException, IOException
	{
		SSLEngineResult _hRes;
		if (cipherSendBuffer.position() != 0)
		{
			cipherSendBuffer.compact();
		}
		_hRes = sslEngine.wrap(appSendBuffer, cipherSendBuffer);
		hStatus = _hRes.getHandshakeStatus();
		cipherSendBuffer.flip();
		return sChannel.write(cipherSendBuffer);
	}

	private void executeTasks()
	{
		Runnable _r = null;
		while ((_r = sslEngine.getDelegatedTask()) != null)
		{
			new Thread(_r).start();
		}

		hStatus = sslEngine.getHandshakeStatus();
	}
	
	private SSLContext getDefaultSSLContext()
	{
		try
		{
			SSLContext _sslCtx = SSLContext.getInstance("SSL");
			_sslCtx.init(null, new TrustManager[] { new X509TrustManager()
			{
				public java.security.cert.X509Certificate[] getAcceptedIssuers()
				{
					return null;
				}

				public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
				{
				}

				public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
				{
				}
			} }, new SecureRandom());
			
			return _sslCtx;
		}
		catch (Exception aExc)
		{
			throw new RuntimeException(aExc);
		}
	}
}