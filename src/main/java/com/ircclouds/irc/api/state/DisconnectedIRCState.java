package com.ircclouds.irc.api.state;

import java.util.*;

import com.ircclouds.irc.api.domain.*;

public class DisconnectedIRCState implements IIRCState
{
	@Override
	public String getNickname()
	{
		return null;
	}

	@Override
	public List<String> getAltNicks()
	{
		return null;
	}

	@Override
	public String getRealname()
	{
		return null;
	}

	@Override
	public String getIdent()
	{
		return null;
	}

	@Override
	public List<IRCChannel> getChannels()
	{
		return null;
	}

	@Override
	public IRCChannel getChannelByName(String aChannelName)
	{
		return null;
	}

	@Override
	public IRCServer getServer()
	{
		return null;
	}

	@Override
	public IRCServerOptions getServerOptions()
	{
		return new IRCServerOptions(new Properties());
	}

	@Override
	public boolean isConnected()
	{
		return false;
	}

	@Override
	public IIRCState getPrevious()
	{
		return null;
	}
}
