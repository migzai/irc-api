package com.ircclouds.irc.api;

import java.io.*;

import com.ircclouds.irc.api.commands.*;
import com.ircclouds.irc.api.comms.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.listeners.*;

public abstract class AbstractIRCSession implements IIRCSession
{
	private IMessagesDispatcher dispatcher = new MessagesDispatcherImpl();
	private ICommandServer cmdServ;
	private IMessagesReader reader;
	private AbstractApiDaemon daemon;
	private IConnection conn;

	public AbstractIRCSession()
	{
		cmdServ = new AbstractCommandServerImpl()
		{
			@Override
			public IConnection getConnection()
			{
				return conn;
			}
		};
		
		reader = new AbstractMessagesReader()
		{
			@Override
			protected IRCServerOptions getIRCServerOptions()
			{
				return AbstractIRCSession.this.getIRCServerOptions();
			}

			@Override
			public IConnection getConnection()
			{
				return conn;
			}
		};
		
		daemon = new AbstractApiDaemon(reader, dispatcher, gettMessageFilter())
		{
			@Override
			protected void onExit()
			{
				try
				{
					close();
				}
				catch (IOException aExc)
				{
					throw new RuntimeException(aExc);
				}
			}
		};
	}

	public void execute(ICommand aCommand) throws IOException
	{
		cmdServ.execute(aCommand);
	}

	public void addListeners(IMessageListener... aListeners)
	{
		for (IMessageListener _listener : aListeners)
		{
			dispatcher.register(_listener);
		}
	}

	@Override
	public void removeListener(IMessageListener aListener)
	{
		dispatcher.unregister(aListener);
	}

	@Override
	public boolean open(IRCServer aServer) throws IOException
	{
		if (!aServer.isSSL())
		{
			conn = new SocketChannelConnection();
		}
		else
		{
			conn = new SSLSocketChannelConnection();
		}
		
		if (conn.open(aServer.getHostname(), aServer.getPort()))
		{
			if (!daemon.isAlive())
			{
				daemon.start();	
			}
			
			return true;
		}

		return false;
	}

	@Override
	public void close() throws IOException
	{
		conn.close();
		
		reader.reset();
	}

	protected abstract IRCServerOptions getIRCServerOptions();
}
