package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

/**
 * 
 * @author
 * 
 */
public class UserNotice extends AbstractNotice implements IUserMessage
{
	private IRCUser fromUser;

	public UserNotice(IRCUser aFromUser, String aText)
	{
		super(aText);
		
		fromUser = aFromUser;
	}
	
	public IRCUser getFromUser()
	{
		return fromUser;
	}
}
