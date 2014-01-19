package com.ircclouds.irc.api.domain.messages;

import java.util.*;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;
import com.ircclouds.irc.api.utils.*;

/**
 * 
 * @author
 * 
 */
public class ChannelModeMessage implements IMessage
{
	private ISource user;
	private String channel;
	private String modeStr;
	private List<ChannelMode> addedModes;
	private List<ChannelMode> removedModes;

	public ChannelModeMessage(ISource aUser, String aChanName, String aModeStr, List<ChannelMode> aAddedModes, List<ChannelMode> aRemModes)
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

	public ISource getSource()
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

	@Override
	public String asRaw()
	{
		// TODO: Account for modes with parameters
		return new StringBuffer().append(":").append(user).append(" MODE ").append(channel).append(" +").append(StringUtils.join(addedModes)).append("-").append(StringUtils.join(removedModes)).toString();
	}
}
