package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class ChannelNotice extends UserNotice implements IChannelMessage
{
	public ChannelNotice(IRCUser aFromUser, String aText, String aChannelName)
	{
		super(aFromUser, aText, aChannelName);
	}

	@Override
	public String getChannelName()
	{
		return target;
	}
}
