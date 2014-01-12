package com.ircclouds.irc.api.domain;

public class ChannelModeC extends ChannelMode
{
	private String param;
	
	public ChannelModeC(Character aType)
	{
		this(aType, "");
	}
	
	public ChannelModeC(Character aType, String aParam)
	{
		super(aType);
		
		param = aParam;
	}
	
	public String getParam()
	{
		return param;
	}
}
