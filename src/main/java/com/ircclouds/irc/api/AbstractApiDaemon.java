package com.ircclouds.irc.api;

import java.io.*;

import org.slf4j.*;

import com.ircclouds.irc.api.comms.IConnection.EndOfStreamException;
import com.ircclouds.irc.api.domain.messages.ClientErrorMessage;
import com.ircclouds.irc.api.domain.messages.interfaces.*;
import com.ircclouds.irc.api.filters.*;

public abstract class AbstractApiDaemon extends Thread
{
	private static final Logger LOG = LoggerFactory.getLogger(AbstractApiDaemon.class);

	private final IMessageReader reader;
	private final IMessageDispatcher dispatcher;

	public AbstractApiDaemon(IMessageReader aReader, IMessageDispatcher aDispatcher)
	{
		super("ApiDaemon");

		reader = aReader;
		dispatcher = aDispatcher;
	}

	@Override
	public void run()
	{
		try
		{
			while (reader.available())
			{
				IMessage _msg = reader.readMessage();
				if (_msg != null)
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
				}
			}
		}
		catch (EndOfStreamException aExc)
		{
			LOG.debug("Received end of stream, closing connection", aExc);
			// Signaling the exception to the api is necessary, since there is a
			// chance that the connection is abruptly cut off before connection
			// (or rather IRC registration process) is finished. Otherwise,
			// there would be no feedback of the connection failure.
			signalExceptionToApi(aExc);
			dispatcher.dispatch(new ClientErrorMessage(aExc), TargetListeners.ALL);
		}
		catch (IOException aExc)
		{
			LOG.error(this.getName(), aExc);
			signalExceptionToApi(aExc);
			dispatcher.dispatch(new ClientErrorMessage(aExc), TargetListeners.ALL);
		}
		finally
		{
			LOG.debug("ApiDaemon Exiting..");

			onExit();
		}
	}

	protected abstract void signalExceptionToApi(Exception aExc);

	protected abstract void onExit();
	
	protected abstract IMessageFilter getMessageFilter();
}
