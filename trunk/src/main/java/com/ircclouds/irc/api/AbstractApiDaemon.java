package com.ircclouds.irc.api;

import org.slf4j.*;

import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

public abstract class AbstractApiDaemon extends Thread
{
	private static final Logger LOG = LoggerFactory.getLogger(AbstractApiDaemon.class);

	private IMessagesReader reader;
	private IMessagesDispatcher dispatcher;

	public AbstractApiDaemon(IMessagesReader aReader, IMessagesDispatcher aDispatcher)
	{
		super("ApiDaemon");

		reader = aReader;
		dispatcher = aDispatcher;
	}

	public void run()
	{
		try
		{
			while (reader.available())
			{
				for (IMessage _msg : reader.readMessages())
				{
					dispatcher.dispatch(_msg);
					
					if (_msg instanceof ErrorMessage)
					{
						break;
					}
				}
			}
		}
		finally
		{
			LOG.debug("ApiDaemon Exiting..");

			onExit();
		}
	}

	protected abstract void onExit();
}
