package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;

/**
 * 
 * @author
 * 
 */
public class ChannelVersionMsg extends ChannelCTCPMsg
{
	public ChannelVersionMsg(WritableIRCUser aFromUser, String aText, String aChanName)
	{
		super(aFromUser, aText, aChanName);
	}
}
