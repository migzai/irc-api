package com.ircclouds.irc.api.commands;

import com.ircclouds.irc.api.utils.*;

/**
 * 
 * @author
 * 
 */
public class JoinChanCmd implements ICommand
{
	private static final String JOIN = "JOIN";

	private String chanName;
	private String key;

	public JoinChanCmd(String aChanName)
	{
		this(aChanName, "");
	}

	public JoinChanCmd(String aChanName, String aKey)
	{
		chanName = aChanName;
		key = aKey;
	}
	
	public String asString()
	{
		return new StringBuffer().append(JOIN).append(" ").append(getChanName()).append(getKey()).append("\r\n").toString();
	}

	private String getKey()
	{
		return !StringUtils.isEmpty(key) ? " :" + key : "";
	}

	private String getChanName()
	{
		return chanName;
	}
}
