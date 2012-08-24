package com.ircclouds.irc.api.ctcp;

public interface DCCSendResult
{
	int getNumberOfAcksReceived();
	
	int totalBytesSent();
	
	long totalTime();
}