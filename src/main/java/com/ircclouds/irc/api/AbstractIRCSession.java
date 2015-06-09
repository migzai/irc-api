package com.ircclouds.irc.api;

import java.io.*;

import javax.net.ssl.*;

import com.ircclouds.irc.api.commands.*;
import com.ircclouds.irc.api.comms.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.filters.*;
import com.ircclouds.irc.api.listeners.*;
import com.ircclouds.irc.api.state.*;

public abstract class AbstractIRCSession implements IIRCSession
{
	private final IMessageDispatcher dispatcher = new MessageDispatcherImpl();
	private final ICommandServer cmdServ;
	private final IMessageReader reader;
	private final AbstractApiDaemon daemon;
	private IConnection conn;
	private Callback<IIRCState> callback;

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
		
		reader = new AbstractMessageReader()
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
		
		daemon = new AbstractApiDaemon(reader, dispatcher)
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

			@Override
			protected IMessageFilter getMessageFilter()
			{
				return AbstractIRCSession.this.getMessageFilter();
			}

			@Override
			protected void signalExceptionToApi(Exception aExc)
			{
				callback.onFailure(aExc);
			}
		};
	}

	public void execute(ICommand aCommand) throws IOException
	{
		cmdServ.execute(aCommand);
	}

	@Override
	public void addListeners(MESSAGE_VISIBILITY aListenerLevel, IMessageListener... aListeners)
	{
		for (IMessageListener _listener : aListeners)
		{
			dispatcher.register(_listener, aListenerLevel);
		}
	}
	
	@Override
	public ICommandServer getCommandServer()
	{
		return cmdServ;
	}

	@Override
	public void removeListener(IMessageListener aListener)
	{
		dispatcher.unregister(aListener);
	}

	@Override
	public boolean open(IRCServer aServer, Callback<IIRCState> aCallback) throws IOException
	{
		callback = aCallback;
		
		if (!aServer.isSSL())
		{
			conn = new SocketChannelConnection();
		}
		else
		{
			conn = new SSLSocketChannelConnection();
		}
		
		SSLContext _ctx = null;
		if (aServer instanceof SecureIRCServer)
		{
			_ctx = ((SecureIRCServer) aServer).getSSLContext();
		}
		
		if (conn.open(aServer.getHostname(), aServer.getPort(), _ctx, aServer.getProxy(), aServer.isResolveByProxy()))
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

	@Override
	public void dispatchClientError(final Exception e)
	{
		final IMessageDispatcher currentDispatcher = this.dispatcher;
		new Thread()
		{

			@Override
			public void run()
			{
				final ClientErrorMessage errorMsg = new ClientErrorMessage(e);
				currentDispatcher.dispatch(errorMsg, TargetListeners.ALL);
			}
		}.start();
	}
}
