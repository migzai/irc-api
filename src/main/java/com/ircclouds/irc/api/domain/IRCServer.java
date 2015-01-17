package com.ircclouds.irc.api.domain;

import com.ircclouds.irc.api.domain.messages.interfaces.*;
import java.net.Proxy;

/**
 * 
 * @author
 * 
 */
public class IRCServer implements ISource
{
	private static final int DEFAULT_IRC_SERVER_PORT = 6667;
	private static final int DEFAULT_SSL_IRC_SERVER_PORT = 6697;

	private final String hostname;
	private final String password;
	private final int port;
	private final Boolean isSSL;
	private final Proxy proxy;
	private final boolean resolveByProxy;

	public IRCServer(String aHostname)
	{
		this(aHostname, DEFAULT_IRC_SERVER_PORT);
	}

	public IRCServer(String aHostname, Boolean aSSLServer)
	{
		if (aSSLServer)
		{
			port = DEFAULT_SSL_IRC_SERVER_PORT;
		}
		else
		{
			port = DEFAULT_IRC_SERVER_PORT;
		}
		hostname = aHostname;
		password = "";
		isSSL = aSSLServer;
		proxy = null;
		resolveByProxy = false;
	}

	public IRCServer(String aHostname, int aPort)
	{
		this(aHostname, aPort, "", false, null, false);
	}

	public IRCServer(String aHostname, int aPort, Boolean aIsSSL)
	{
		this(aHostname, aPort, "", aIsSSL, null, false);
	}

	public IRCServer(String aHostname, int aPort, String aPassword, Boolean aIsSSL)
	{
		this(aHostname, aPort, aPassword, aIsSSL, null, false);
	}

	public IRCServer(String aHostname, int aPort, String aPassword, Boolean aIsSSL, Proxy aProxy, boolean aResolveByProxy)
	{
		hostname = aHostname;
		port = aPort;
		password = aPassword;
		isSSL = aIsSSL;
		proxy = aProxy;
		resolveByProxy = aResolveByProxy;
	}

	public String getPassword()
	{
		return password;
	}

	public String getHostname()
	{
		return hostname;
	}

	public int getPort()
	{
		return port;
	}

	public Boolean isSSL()
	{
		return isSSL;
	}

	public Proxy getProxy()
	{
		return proxy;
	}

	public boolean isResolveByProxy()
	{
		return resolveByProxy;
	}
	
	@Override
	public String toString()
	{
		return hostname;
	}
}