package com.ircclouds.irc.api.ctcp;

public interface DCCSendProgressCallback extends DCCSendCallback
{
	void onProgress(int aBytesTransferred);
}
