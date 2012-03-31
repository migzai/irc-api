package com.ircclouds.irc.api.state;

import java.util.*;

import com.ircclouds.irc.api.domain.*;

public interface IIRCState
{
	String getNickname();

	List<String> getAltNicks();

	String getRealname();

	String getIdent();

	List<IRCChannel> getChannels();

	IRCChannel getChannelByName(String aChannelName);

	IRCServer getServer();

	IRCServerOptions getServerOptions();
	
	boolean isConnected();
}
