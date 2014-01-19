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
	IRCUser fromUser;
	String target;

	public UserNotice(IRCUser aFromUser, String aText, String aTarget)
	{
		super(aText);
		
		target = aTarget;
		fromUser = aFromUser;
	}
	
	public IRCUser getSource()
	{
		return fromUser;
	}
	
	@Override
	public String asRaw()
	{
		return new StringBuffer().append(":").append(fromUser).append(" NOTICE ").append(target).append(" :").append(text).toString();
	}
}
