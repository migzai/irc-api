package com.ircclouds.irc.api.domain;

import java.net.*;

import javax.net.ssl.*;

public class SecureIRCServer extends IRCServer
{
	private SSLContext sslContext;

	public SecureIRCServer(String aHostname)
	{
		super(aHostname, true);
	}

	public SecureIRCServer(String aHostname, int aPort)
	{
		super(aHostname, aPort, "", true);
	}

	public SecureIRCServer(String aHostname, int aPort, String aPassword)
	{
		super(aHostname, aPort, aPassword, true);
	}

	public SecureIRCServer(String aHostname, SSLContext aSSLContext)
	{
		super(aHostname, true);

		sslContext = aSSLContext;
	}

	public SecureIRCServer(String aHostname, int aPort, SSLContext aSSLContext)
	{
		super(aHostname, aPort, "", true);
	
		sslContext = aSSLContext;
	}

	public SecureIRCServer(String aHostname, int aPort, SSLContext aSSLContext, Proxy aProxy)
	{
		super(aHostname, aPort, "", true, aProxy);

		sslContext = aSSLContext;
	}

	public SecureIRCServer(String aHostname, int aPort, String aPassword, SSLContext aSSLContext)
	{
		super(aHostname, aPort, aPassword, true);

		sslContext = aSSLContext;
	}

	public SecureIRCServer(String aHostname, int aPort, String aPassword, SSLContext aSSLContext, Proxy aProxy)
	{
		super(aHostname, aPort, aPassword, true, aProxy);

		sslContext = aSSLContext;
	}

	public SSLContext getSSLContext()
	{
		return sslContext;
	}
}
