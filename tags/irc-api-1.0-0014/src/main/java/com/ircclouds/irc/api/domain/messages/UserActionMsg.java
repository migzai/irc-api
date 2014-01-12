package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;

public class UserActionMsg extends UserPrivMsg
{
	public UserActionMsg(WritableIRCUser aFromUser, String aToUser, String aText)
	{
		super(aFromUser, aToUser, aText);
	}
}
