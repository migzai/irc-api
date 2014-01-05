package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

/**
 * 
 * @author
 * 
 */
public class QuitMessage implements IUserMessage
{
	private WritableIRCUser user;
	private String quitMsg;

	public QuitMessage(WritableIRCUser aFromUser, String aQuitMsg)
	{
		user = aFromUser;
		quitMsg = aQuitMsg;
	}
	
	public WritableIRCUser getSource()
	{
		return user;
	}

	public String getQuitMsg()
	{
		return quitMsg;
	}
}
