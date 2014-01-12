package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;

/**
 * 
 * @author
 * 
 */
public class ChannelCTCPMsg extends ChannelPrivMsg
{
	public ChannelCTCPMsg(WritableIRCUser aFromUser, String aText, String aChanName)
	{
		super(aFromUser, aText, aChanName);
	}
}
