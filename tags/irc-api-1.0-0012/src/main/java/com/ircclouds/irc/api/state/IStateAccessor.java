package com.ircclouds.irc.api.state;

import com.ircclouds.irc.api.domain.*;

public interface IStateAccessor
{
	void saveChan(IRCChannel aChannel);
	
	void deleteChan(String aChannelName);
	
	void updateNick(String aNewNick);
	
	void deleteNickFromChan(String aChan, String aNick);
	
	IIRCState getIRCState();
}
