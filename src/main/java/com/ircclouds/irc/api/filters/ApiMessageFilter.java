package com.ircclouds.irc.api.filters;

import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class ApiMessageFilter implements IMessageFilter
{
	private Integer nextInt;

	public ApiMessageFilter()
	{
		this(0);
	}
	
	public ApiMessageFilter(Integer aNextInt)
	{
		nextInt = aNextInt;
	}
	
	@Override
	public MessageFilterResult filter(IMessage aMsg)
	{
		if (aMsg instanceof ServerMessage)
		{
			String aText = ((ServerMessage) aMsg).getText();
			String cmpnts[] = aText.split(" :");
			if (nextInt+"" == cmpnts[0])
			{
				return new MessageFilterResult(null, FilterStatus.HALT);
			}

		}
		
		return new MessageFilterResult(aMsg, FilterStatus.PASS);
	}

	public Integer getNextInt()
	{
		return nextInt;
	}

	public void setNextInt(Integer aNextInt)
	{
		nextInt = aNextInt;
	}
}
