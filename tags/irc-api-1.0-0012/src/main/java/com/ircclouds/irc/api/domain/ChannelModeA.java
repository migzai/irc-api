package com.ircclouds.irc.api.domain;

public class ChannelModeA extends ChannelMode
{
	private String param;
	
	public ChannelModeA(Character aType, String aParam)
	{
		super(aType);
		
		param = aParam;
	}
	
	public String getParam()
	{
		return param;
	}
}
