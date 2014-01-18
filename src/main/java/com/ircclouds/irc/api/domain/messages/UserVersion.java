package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;

/**
 * 
 * @author
 * 
 */
public class UserVersion extends UserCTCPMsg
{
	public UserVersion(IRCUser aFromUser, String aToUser, String aText)
	{
		super(aFromUser, aToUser, aText);
	}
}
