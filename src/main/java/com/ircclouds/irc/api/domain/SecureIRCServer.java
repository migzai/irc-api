package com.ircclouds.irc.api.domain;

import javax.net.ssl.SSLContext;

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
	
	public SecureIRCServer(String aHostname, int aPort, String aPassword, SSLContext aSSLContext)
	{
		super(aHostname, aPort, aPassword, true);
		
		sslContext = aSSLContext;
	}	
	
	public SSLContext getSSLContext()
	{
			return sslContext;		
	}
}
