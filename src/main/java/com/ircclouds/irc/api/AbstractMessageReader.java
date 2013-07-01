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

public abstract class AbstractMessageReader implements IMessageReader, INeedsConnection
{
	private static final Logger LOG = LoggerFactory.getLogger(AbstractMessageReader.class);
	private static final String CRLF = "\r\n";

	private AbstractMessageFactory msgFactory;
	private String serverMsg = "";
	private Queue<String> strMessages = new LinkedList<String>();
	private boolean canRead = true;
	
	public AbstractMessageReader()
	{
		msgFactory = new AbstractMessageFactory()
		{
			@Override
			protected IRCServerOptions getIRCServerOptions()
			{
				return AbstractMessageReader.this.getIRCServerOptions();
			}
		};
	}

	public boolean available()
	{
		try
		{
			if (canRead)
			{
				serverMsg += getConnection().read();				
				canRead = false;
				fetchNextBatch();
			}

			return !strMessages.isEmpty() || !serverMsg.isEmpty();
		}
		catch (IOException aExc)
		{
			LOG.error("Error reading from connection", aExc);
			return false;
		}
	}

	public IMessage readMessage()
	{
		IMessage _msg = IMessage.NULL_MESSAGE;
		
		if (strMessages.peek() != null)
		{
			try 
			{
				_msg =  msgFactory.build(strMessages.poll());
			}
			catch (IRCOMException aExc)
			{
				LOG.error("Error from the OM layer", aExc);
			}
		}
		
		canRead = strMessages.isEmpty();
		
		return _msg;
	}

	@Override
	public void reset()
	{
		strMessages.clear();
		serverMsg = "";
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

			strMessages.addAll(Arrays.asList(_tempMsg.split(CRLF)));
		}
	}
}
