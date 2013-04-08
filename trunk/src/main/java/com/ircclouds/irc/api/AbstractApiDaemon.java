package com.ircclouds.irc.api;

import org.slf4j.*;

import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;
import com.ircclouds.irc.api.filters.*;

public abstract class AbstractApiDaemon extends Thread
{
	private static final Logger LOG = LoggerFactory.getLogger(AbstractApiDaemon.class);

	private IMessagesReader reader;
	private IMessageDispatcher dispatcher;

	public AbstractApiDaemon(IMessagesReader aReader, IMessageDispatcher aDispatcher)
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
					dispatcher.dispatchToPrivateListeners(_msg);
					
					if (getMessageFilter() != null)
					{
						MessageFilterResult _fr = getMessageFilter().filter(_msg);
						if (_fr.getFilterStatus().equals(FilterStatus.PASS))
						{
							dispatcher.dispatch(_fr.getFilteredMessage(), getMessageFilter().getTargetListeners());
						}
					}
					else
					{
						dispatcher.dispatch(_msg, TargetListeners.ALL);
					}
					
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
	
	protected abstract IMessageFilter getMessageFilter();
}
