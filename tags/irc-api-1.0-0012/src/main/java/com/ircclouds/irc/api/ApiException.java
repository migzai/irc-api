package com.ircclouds.irc.api;

public class ApiException extends RuntimeException
{
	public ApiException(Exception aExc)
	{
		super(aExc);
	}

	public ApiException(String aString)
	{
		super(aString);
	}
}
