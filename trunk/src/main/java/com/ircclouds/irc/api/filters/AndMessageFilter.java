package com.ircclouds.irc.api.filters;

import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class AndMessageFilter implements IMessageFilter
{
	private IMessageFilter filterOne;
	private IMessageFilter filterTwo;
	
	public AndMessageFilter(IMessageFilter aFilterOne, IMessageFilter aFilterTwo)
	{
		filterOne = aFilterOne;
		filterTwo = aFilterTwo;
	}
	
	@Override
	public MessageFilterResult filter(IMessage aMessage)
	{
		MessageFilterResult _fr1 = filterOne.filter(aMessage);
		if (_fr1.getFilterStatus().equals(FilterStatus.PASS))
		{	
			return filterTwo.filter(_fr1.getFilteredMessage());	
		}
		
		return _fr1;
	}
}
