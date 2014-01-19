package com.ircclouds.irc.api.domain;

import java.io.*;
import java.util.*;

public class IRCTopic implements Serializable
{	
	Date date;
	String setBy;
	String value;
	
	public IRCTopic()
	{
		this("");
	}
	
	public IRCTopic(String aValue)
	{
		this("", aValue);
	}
	
	public IRCTopic(String aSetBy, String aValue)
	{
		this(aSetBy, null, aValue);
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
}
