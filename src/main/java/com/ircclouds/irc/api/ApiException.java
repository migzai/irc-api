package com.ircclouds.irc.api;

import java.io.*;

public class ApiException extends RuntimeException
{
	public ApiException(IOException aExc)
	{
		super(aExc);
	}

	public ApiException(String aString)
	{
		super(aString);
	}
}
