package com.ircclouds.irc.api.ctcp;

import java.io.*;

import com.ircclouds.irc.api.*;

public class DCCReceiveException extends ApiException
{
	private DCCReceiveResult result;
	
	private IOException exc;
	
	public DCCReceiveException(DCCReceiveResult aResult, IOException aException)
	{
		super(DCCReceiveException.class.getName());
		
		result = aResult;
		exc = aException;
	}
	
	public IOException getException()
	{
		return exc;
	}
	
	public DCCReceiveResult getDCCReceiveResult()
	{
		return result;
	}
}
