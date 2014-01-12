package com.ircclouds.irc.api.ctcp;

public interface DCCReceiveProgressCallback extends DCCReceiveCallback
{
	void onProgress(int aBytesTransferred);
}
