package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class ChannelPrivMsg extends AbstractPrivMsg implements IChannelMessage
{
	private String channelName;

	public ChannelPrivMsg(WritableIRCUser aFromUser, String aText, String aChanName)
	{
		super(aFromUser, aText);
		channelName = aChanName;
	}
	
	public String getChannelName()
	{
		return channelName;
	}
}
