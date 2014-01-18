package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;

/**
 * 
 * @author
 * 
 */
public class UserCTCPMsg extends UserPrivMsg
{
	public UserCTCPMsg(IRCUser aFromUser, String aToUser, String aText)
	{
		super(aFromUser, aToUser, aText);
	}
}
