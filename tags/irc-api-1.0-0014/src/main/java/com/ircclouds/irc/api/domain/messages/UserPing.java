package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;

public class UserPing extends UserCTCPMsg
{
	public UserPing(WritableIRCUser aFromUser, String aToUser, String aText)
	{
		super(aFromUser, aToUser, aText);
	}
}
