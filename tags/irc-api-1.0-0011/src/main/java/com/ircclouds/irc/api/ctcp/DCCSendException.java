package com.ircclouds.irc.api.ctcp;

import com.ircclouds.irc.api.*;

public class DCCSendException extends ApiException
{
	private DCCSendResult result;
	
	private Exception readerExc;
	private Exception writerExc;
	
	public DCCSendException(DCCSendResult aResult, Exception aReaderException, Exception aWriterException)
	{
		super(DCCSendException.class.getName());
		
		result = aResult;
		
		readerExc = aReaderException;
		writerExc = aWriterException;
	}
	
	public DCCSendResult getDCCSendResult()
	{
		return result;
	}
	
	public Exception getReaderException()
	{
		return readerExc;
	}
	
	public Exception getWriterException()
	{
		return writerExc;
	}
}
