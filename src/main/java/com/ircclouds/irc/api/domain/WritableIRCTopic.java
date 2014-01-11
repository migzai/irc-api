package com.ircclouds.irc.api.domain;

import java.util.*;

public class WritableIRCTopic extends IRCTopic
{
	public WritableIRCTopic(String aTopic)
	{
		super(aTopic);
	}

	public WritableIRCTopic(String setBy, Date date, String value)
	{
		super(setBy, date, value);
	}

	public void setValue(String aValue)
	{
		value = aValue;		
	}
	
	public void setSetBy(String aSetBy)
	{
		setBy = aSetBy;
	}
	
	public void setDate(Date aDate)
	{
		date = aDate;
	}
}