package com.ircclouds.irc.api.filters;

import com.ircclouds.irc.api.domain.messages.interfaces.*;

public abstract class AbstractAndMessageFilter implements IMessageFilter
{
	private IMessageFilter filterOne;
	
	public AbstractAndMessageFilter(IMessageFilter aFilterOne)
	{
		filterOne = aFilterOne;
	}

	@Override
	public MessageFilterResult filter(IMessage aMessage)
	{
		MessageFilterResult _fr1 = filterOne.filter(aMessage);
		if (_fr1.getFilterStatus().equals(FilterStatus.PASS))
		{	
			return getSecondFilter().filter(_fr1.getFilteredMessage());	
		}
		
		return _fr1;
	}
	
	protected abstract IMessageFilter getSecondFilter();
}
