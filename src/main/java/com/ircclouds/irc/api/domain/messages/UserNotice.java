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
	private WritableIRCUser fromUser;

	public UserNotice(WritableIRCUser aFromUser, String aText)
	{
		super(aText);
		
		fromUser = aFromUser;
	}
	
	public WritableIRCUser getSource()
	{
		return fromUser;
	}
}
