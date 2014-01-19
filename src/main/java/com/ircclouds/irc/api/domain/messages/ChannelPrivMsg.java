package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class ChannelPrivMsg extends AbstractPrivMsg implements IChannelMessage
{
	String channelName;

	public ChannelPrivMsg(IRCUser aFromUser, String aText, String aChanName)
	{
		super(aFromUser, aText);
		channelName = aChanName;
	}
	
	public String getChannelName()
	{
		return channelName;
	}
	
	@Override
	public String asRaw()
	{
		return new StringBuffer().append(":").append(fromUser).append(" PRIVMSG ").append(channelName).append(" :").append(text).toString();
	}
}
