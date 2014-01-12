package com.ircclouds.irc.api.filters;

import java.util.*;

import com.ircclouds.irc.api.listeners.*;

public class TargetListeners
{
	public static final TargetListeners ALL = new TargetListeners(new ArrayList<IMessageListener>(), HowMany.ALL);
	
	private List<IMessageListener> listeners;
	private HowMany howMany;
	
	public TargetListeners(List<IMessageListener> aListeners, HowMany aHowMany)
	{
		listeners = aListeners;
		howMany = aHowMany;
	}
	
	public List<IMessageListener> getListeners()
	{
		return listeners;
	}
	
	public HowMany getHowMany()
	{
		return howMany;
	}
}
