package com.ircclouds.irc.api;

import java.io.*;
import java.util.*;

import org.slf4j.*;

import com.ircclouds.irc.api.comms.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;
import com.ircclouds.irc.api.om.*;

/**
 * 
 * @author miguel
 * 
 */

public abstract class AbstractMessagesReader implements IMessagesReader, INeedsConnection
{
	private static final Logger LOG = LoggerFactory.getLogger(AbstractMessagesReader.class);
	private static final String CRLF = "\r\n";

	private AbstractMessageFactory msgFactory;
	private String serverMsg = "";
	private List<IMessage> messages = new ArrayList<IMessage>();

	private boolean canRead = true;
	
	public AbstractMessagesReader()
	{
		msgFactory = new AbstractMessageFactory()
		{
			@Override
			protected IRCServerOptions getIRCServerOptions()
			{
				return AbstractMessagesReader.this.getIRCServerOptions();
			}
		};
	}

	public boolean available()
	{
		try
		{
			if (messages.isEmpty())
			{
				if (canRead)
				{
					serverMsg += getConnection().read();
					canRead = false;
				}

				return !serverMsg.isEmpty();
			}

			return true;
		}
		catch (IOException aExc)
		{
			LOG.error("Error reading from connection", aExc);
			return false;
		}
	}

	public List<IMessage> readMessages()
	{
		canRead = true;
		fetchNextBatch();

		List<IMessage> _messages = new ArrayList<IMessage>(messages);
		messages.clear();

		return _messages;
	}

	@Override
	public void reset()
	{
		messages.clear();
		
		canRead = true;
	}
	
	protected abstract IRCServerOptions getIRCServerOptions();
	
	private void fetchNextBatch()
	{
		if (serverMsg.contains(CRLF))
		{
			String _tempMsg = serverMsg;
			if (!serverMsg.endsWith(CRLF))
			{
				int _i = serverMsg.lastIndexOf(CRLF);
				_tempMsg = serverMsg.substring(0, _i);
				serverMsg = serverMsg.substring(_i + CRLF.length());
			}
			else
			{
				serverMsg = "";
			}

			for (String _msg : _tempMsg.split(CRLF))
			{
				try
				{
					IMessage _iMsg = msgFactory.build(_msg);
					if (_iMsg != IMessage.NO_MESSAGE)
					{
						messages.add(_iMsg);
					}	
				}
				catch (IRCOMException aExc)
				{
					LOG.error("Error from the OM layer", aExc);
				}
			}
		}
	}
}
