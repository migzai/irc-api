package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.messages.interfaces.*;

public abstract class AbstractPingMessage implements IMessage
{
	private String replyText;

	public String getReplyText()
	{
		return replyText;
	}

	public void setReplyText(String aReplyText)
	{
		replyText = aReplyText;
	}

}
