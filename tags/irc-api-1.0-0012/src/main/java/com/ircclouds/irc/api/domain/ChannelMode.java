package com.ircclouds.irc.api.domain;

public class ChannelMode
{
	private Character type;

	public ChannelMode(Character aType)
	{
		type = aType;
	}
	
	public Character getChannelModeType()
	{
		return type;
	}
	
	public boolean equals(Object aChanMode)
	{
		if (aChanMode != null && aChanMode instanceof ChannelMode)
		{
			ChannelMode _cm = (ChannelMode) aChanMode;
			return type.equals(_cm.getChannelModeType());
		}
		
		return false;
	}
	
	public int hashCode()
	{
		return type.hashCode();
	}
	
	public String toString()
	{
		return "[ChannelMode[type: " + type.toString() + "]]";
	}
}
