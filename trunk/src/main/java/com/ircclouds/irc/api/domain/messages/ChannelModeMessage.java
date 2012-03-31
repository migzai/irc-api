package com.ircclouds.irc.api.domain.messages;

import java.util.*;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

/**
 * 
 * @author
 * 
 */
public class ChannelModeMessage implements IChannelMessage
{
	private IRCUser user;
	private String channel;
	private String modeStr;
	private List<ChannelMode> addedModes;
	private List<ChannelMode> removedModes;

	public ChannelModeMessage(IRCUser aUser, String aChanName, String aModeStr, List<ChannelMode> aAddedModes, List<ChannelMode> aRemModes)
	{
		user = aUser;
		channel = aChanName;
		modeStr = aModeStr;
		addedModes = aAddedModes;
		removedModes = aRemModes;
	}
	
	public String getChannelName()
	{
		return channel;
	}

	public IRCUser getFromUser()
	{
		return user;
	}

	public List<ChannelMode> getAddedModes()
	{
		return addedModes;
	}

	public List<ChannelMode> getRemovedModes()
	{
		return removedModes;
	}

	public String getModeStr()
	{
		return modeStr;
	}
}
