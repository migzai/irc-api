package com.ircclouds.irc.api;

import java.io.*;
import java.util.*;

import org.slf4j.*;

import com.ircclouds.irc.api.comms.IConnection.EndOfStreamException;
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
	private StringBuilder ircData = new StringBuilder();
	private Queue<String> ircMessages = new LinkedList<String>();
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
				ircData.append(getConnection().read());				
				canRead = false;
				fetchNextBatch();
			}

			return true;
		}
		catch (EndOfStreamException aExc)
		{
			LOG.error("End of stream received.");
			return false;
		}
		catch (IOException aExc)
		{
			getConnection().setReadError();
			LOG.error("Error reading from connection", aExc);
			return false;
		}
	}

	public IMessage readMessage()
	{
		IMessage _msg = IMessage.NULL_MESSAGE;
		
		if (ircMessages.peek() != null)
		{
			try 
			{
				_msg =  msgFactory.build(ircMessages.poll());
			}
			catch (IRCOMException aExc)
			{
				LOG.error("Error from the OM layer", aExc);
			}
		}
		
		canRead = ircMessages.isEmpty();
		
		return _msg;
	}

	@Override
	public void reset()
	{
		ircMessages.clear();
		ircData.setLength(0);
		canRead = true;
	}
	
	protected abstract IRCServerOptions getIRCServerOptions();
	
	private void fetchNextBatch()
	{
		if (ircData.indexOf(CRLF) != -1)
		{
			String _tempMsg = ircData.toString();
			if (ircData.lastIndexOf(CRLF) != ircData.length() - CRLF.length() - 1)
			{
				int _i = ircData.lastIndexOf(CRLF);
				_tempMsg = ircData.substring(0, _i);
				ircData = new StringBuilder(ircData.substring(_i + CRLF.length()));
			}
			else
			{
				ircData.setLength(0);
			}

			ircMessages.addAll(Arrays.asList(_tempMsg.split(CRLF)));
		}
	}
}