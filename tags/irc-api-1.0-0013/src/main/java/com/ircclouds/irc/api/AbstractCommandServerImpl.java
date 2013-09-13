package com.ircclouds.irc.api;

import java.io.*;

import org.slf4j.*;

import com.ircclouds.irc.api.commands.*;
import com.ircclouds.irc.api.comms.*;

public abstract class AbstractCommandServerImpl implements ICommandServer, INeedsConnection
{
	private static final Logger LOG = LoggerFactory.getLogger(AbstractCommandServerImpl.class);

	public void execute(ICommand aCommand) throws IOException
	{
		LOG.debug("Executing Command: " + aCommand.asString());

		String _str = aCommand.asString() + "\r\n";
		int _written = getConnection().write(_str);
		if (_str.length() > _written)
		{
			LOG.error("Expected to write " + _str.length() + " bytes, but got" + _written);
		}
	}
}
