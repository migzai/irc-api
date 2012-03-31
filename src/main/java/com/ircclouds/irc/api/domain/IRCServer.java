package com.ircclouds.irc.api.domain;

/**
 * 
 * @author
 * 
 */
public class IRCServer
{
	private static final int DEFAULT_IRC_SERVER_PORT = 6667;
	private static final int DEFAULT_SSL_IRC_SERVER_PORT = 6697;

	private String hostname;
	private String password;
	private int port = DEFAULT_IRC_SERVER_PORT;
	private Boolean isSSL;

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
	}

	public IRCServer(String aHostname, int aPort)
	{
		this(aHostname, aPort, "", false);
	}

	public IRCServer(String aHostname, int aPort, Boolean aIsSSL)
	{
		this(aHostname, aPort, "", aIsSSL);
	}

	public IRCServer(String aHostname, int aPort, String aPassword, Boolean aIsSSL)
	{
		hostname = aHostname;
		port = aPort;
		password = aPassword;
		isSSL = aIsSSL;
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
}