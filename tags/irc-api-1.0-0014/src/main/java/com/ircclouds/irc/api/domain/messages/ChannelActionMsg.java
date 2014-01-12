package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;

public class ChannelActionMsg extends ChannelPrivMsg
{
	public ChannelActionMsg(WritableIRCUser aFromUser, String aText, String aChanName)
	{
		super(aFromUser, aText, aChanName);
	}
}
