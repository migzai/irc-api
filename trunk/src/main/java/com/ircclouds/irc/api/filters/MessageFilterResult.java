package com.ircclouds.irc.api.filters;

import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class MessageFilterResult
{
	public static final MessageFilterResult HALT_MSG_RESULT = new MessageFilterResult(null, FilterStatus.HALT);
	
	private IMessage message; 
	private FilterStatus status;
			
	public MessageFilterResult(IMessage aMessage, FilterStatus aStatus)
	{
		message = aMessage;
		status = aStatus;
	}
	
	public IMessage getFilteredMessage()
	{
		return message;
	}
	
	public FilterStatus getFilterStatus()
	{
		return status;
	}
}