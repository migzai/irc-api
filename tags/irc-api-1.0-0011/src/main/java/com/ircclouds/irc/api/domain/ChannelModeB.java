package com.ircclouds.irc.api.domain;

public class ChannelModeB extends ChannelMode
{
	private String param;
	
	public ChannelModeB(Character aType, String aParam)
	{
		super(aType);
		
		param = aParam;
	}
	
	public String getParam()
	{
		return param;
	}
}
