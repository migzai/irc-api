package com.ircclouds.irc.api.state;

import com.ircclouds.irc.api.domain.*;

public interface ISaveState
{
	void save(IRCChannel aChannel);
	
	void delete(String aChannelName);
	
	IIRCState getIRCState();
}
