package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

/**
 * 
 * @author
 * 
 */
public abstract class AbstractPrivMsg implements IUserMessage, IHasText
{
	private WritableIRCUser fromUser;
	private String text;

	public AbstractPrivMsg(WritableIRCUser aFromUser, String aText)
	{
		fromUser = aFromUser;
		text = aText;
	}
	
	public WritableIRCUser getSource()
	{
		return fromUser;
	}

	public String getText()
	{
		return text;
	}
}