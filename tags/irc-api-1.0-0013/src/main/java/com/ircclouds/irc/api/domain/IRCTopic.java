package com.ircclouds.irc.api.domain;

import java.io.*;
import java.util.*;

/**
 * 
 * @author miguel
 * 
 */
public class IRCTopic implements Serializable
{
	private String setBy;
	private Date date;
	private String value;
	
	public IRCTopic(String aValue)
	{
		value = aValue;
	}
	
	public IRCTopic(String aSetBy, Date aDate, String aValue)
	{
		setBy = aSetBy;
		date = aDate;
		value = aValue;
	}

	public String getValue()
	{
		return value;
	}

	public String getSetBy()
	{
		return setBy;
	}
	
	public Date getDate()
	{
		return date;
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
