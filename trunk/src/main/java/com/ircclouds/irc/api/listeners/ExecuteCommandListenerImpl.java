package com.ircclouds.irc.api.listeners;

import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.state.*;

public class ExecuteCommandListenerImpl extends AbstractExecuteCommandListener
{
	private ISaveState updater;
	
	public ExecuteCommandListenerImpl(IIRCSession aSession, ISaveState aUpdater)
	{
		super(aSession);
		
		updater = aUpdater;
	}

	@Override
	public void save(IRCChannel aChannel)
	{
		updater.save(aChannel);
	}

	@Override
	public void delete(String aChannelName)
	{
		updater.delete(aChannelName);
	}

	@Override
	public IIRCState getIRCState()
	{
		return updater.getIRCState();
	}

	@Override
	public void updateNick(String aNewNick)
	{
		updater.updateNick(aNewNick);
	}
}
