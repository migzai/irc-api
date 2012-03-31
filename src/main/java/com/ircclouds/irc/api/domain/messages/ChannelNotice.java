package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

/**
 * 
 * @author
 * 
 */
public class ChannelNotice extends UserNotice implements IChannelMessage
{
	private String channelName;

	public ChannelNotice(IRCUser aFromUser, String aText, String aChannelName)
	{
		super(aFromUser, aText);
		
		channelName = aChannelName;
	}
	
	public String getChannelName()
	{
		return channelName;
	}
}
