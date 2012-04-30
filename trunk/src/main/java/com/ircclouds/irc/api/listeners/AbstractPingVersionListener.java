package com.ircclouds.irc.api.listeners;

import java.io.*;

import org.slf4j.*;

import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.commands.*;
import com.ircclouds.irc.api.domain.messages.*;

public abstract class AbstractPingVersionListener extends VariousMessageListenerAdapter
{
	private static final Logger LOG = LoggerFactory.getLogger(AbstractPingVersionListener.class);

	@Override
	public void onUserPing(UserPing aMsg)
	{
		execute(new SendUserPingReplyCmd(new UserPong(aMsg.getFromUser().getNick(), aMsg.getText())));
	}

	@Override
	public void onUserVersion(UserVersion aMsg)
	{
		execute(new SendVersionReplyCmd(aMsg.getFromUser().getNick()));
	}

	@Override
	public void onServerPing(ServerPing aMsg)
	{
		execute(new SendServerPingReplyCmd(new ServerPong(aMsg.getReplyText())));
	}

	protected abstract IIRCSession getSession();
	
	private void execute(ICommand aCmd)
	{
		try
		{
			getSession().getCommandServer().execute(aCmd);
		}
		catch (IOException aExc)
		{
			LOG.error("Error Executing Command [" + aCmd.asString() + "]", aExc);
		}
	}
}
