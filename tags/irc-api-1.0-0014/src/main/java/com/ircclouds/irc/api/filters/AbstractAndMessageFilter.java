package com.ircclouds.irc.api.filters;

import java.util.*;

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
	
	@Override
	public TargetListeners getTargetListeners()
	{
		if (filterOne.getTargetListeners().getHowMany().equals(HowMany.ALL))
		{
			return getSecondFilter().getTargetListeners();
		}
		else
		{
			if (getSecondFilter().getTargetListeners().getHowMany().equals(HowMany.ALL))
			{
				return filterOne.getTargetListeners();
			}
			else
			{
				return new TargetListeners(getIntersection(filterOne.getTargetListeners().getListeners(), getSecondFilter().getTargetListeners().getListeners()), HowMany.SOME);
			}
		}
	}
	
	private <T> List<T> getIntersection(List<T> aFirstList, List<T> aSecondList)
	{
		List<T> _arrList = new ArrayList<T>();
		for (T _t : aFirstList)
		{
			if (aSecondList.contains(_t))
			{
				_arrList.add(_t);
			}
		}
		
		return _arrList;
	}
	
	protected abstract IMessageFilter getSecondFilter();
}
