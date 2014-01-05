package com.ircclouds.irc.api.domain;

import java.io.*;
import java.util.*;

/**
 * 
 * @author miguel
 * 
 */
public class WritableIRCTopic implements Serializable
{
	private Date date;
	private String setBy;
	private String value;
	
	public WritableIRCTopic()
	{
		this("", "");
	}
	
	public WritableIRCTopic(String aValue)
	{
		this("", aValue);
	}
	
	public WritableIRCTopic(String aSetBy, String aValue)
	{
		setBy = aSetBy;
		value = aValue;
	}
	
	public WritableIRCTopic(String aSetBy, Date aDate, String aValue)
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
