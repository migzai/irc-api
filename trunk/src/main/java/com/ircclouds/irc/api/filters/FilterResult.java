package com.ircclouds.irc.api.filters;

import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class FilterResult
{
	private IMessage message; 
	private FilterStatus status;
	
	public FilterResult(IMessage aMessage, FilterStatus aStatus)
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