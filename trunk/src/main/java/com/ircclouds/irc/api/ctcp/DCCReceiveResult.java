package com.ircclouds.irc.api.ctcp;

public interface DCCReceiveResult
{
	int getNumberOfAcksSent();
	
	int totalBytesReceived();
	
	long totalTime();
}
