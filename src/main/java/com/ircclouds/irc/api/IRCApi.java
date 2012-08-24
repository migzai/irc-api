package com.ircclouds.irc.api;

import java.io.*;
import java.net.*;

import com.ircclouds.irc.api.ctcp.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.filters.*;
import com.ircclouds.irc.api.listeners.*;
import com.ircclouds.irc.api.state.*;

public interface IRCApi
{	
	// Cmds Interface
	void connect(IServerParameters aServerParameters, Callback<IIRCState> aCallback);
	
	void disconnect(String aQuitMessage);

	void joinChannel(String aChannelName);

	void joinChannel(String aChannelName, Callback<IRCChannel> aCallback);

	void joinChannel(String aChannelName, String aKey);
	
	void joinChannel(String aChannelName, String aKey, Callback<IRCChannel> aCallback);

	void leaveChannel(String aChannelName);

	void leaveChannel(String aChannelName, Callback<String> aCallback);

	void leaveChannel(String aChannelName, String aPartMessage);

	void leaveChannel(String aChannelName, String aPartMessage, Callback<String> aCallback);

	void changeNick(String aNewNick);
	
	void changeNick(String aNewNick, Callback<String> aCallback);	
	
//	void notice(String aNick, String aText);
//	
//	void notice(String aNick, String aText, Callback<String> aCallback);	
//			
//	void noticeChannel(String aChannelName, String aMessage);
//
//	void noticeChannel(String aChannelName, String aMessage, Callback<String> aCallback);
//		
	void channelMessage(String aChannelName, String aMessage);

	void channelMessage(String aChannelName, String aMessage, Callback<String> aCallback);
	
	void privateMessage(String aNick, String aText);
	
	void privateMessage(String aNick, String aText, Callback<String> aCallback);	
	
	void actInChannel(String aChannelName, String aActionMessage);

	void actInChannel(String aChannelName, String aActionMessage, Callback<String> aCallback);
	
	void actInPrivate(String aNick, String aActionMessage);
	
	void actInPrivate(String aNick, String aActionMessage, Callback<String> aCallback);

	void changeTopic(String aChannel, String aTopic);
	
	void changeMode(String aModeString);
	
	void sendRawMessage(String aMessage);
	
	// DCC cmds
	void dccSend(String aNick, File aFile, DCCSendCallback aCallback);

	void dccSend(String aNick, File aFile, Integer aTimeout, DCCSendCallback aCallback);
	
	void dccSend(String aNick, Integer aListeningPort, File aFile, DCCSendCallback aCallback);
	
	void dccSend(String aNick, File aFile, Integer aListeningPort, Integer aTimeout, DCCSendCallback aCallback);
	
	void dccAccept(String aNick, File aFile, Integer aPort, Integer aResumePosition, DCCSendCallback aCallback);
	
	void dccAccept(String aNick, File aFile, Integer aPort, Integer aResumePosition, Integer aTimeout, DCCSendCallback aCallback);
	
	void dccReceive(File aFile, Integer aSize, SocketAddress aAddress);
	
	void dccResume(File aFile, Integer aResumePosition, Integer aSize, SocketAddress aAddress);
	
	DCCManager getDCCManager();
	
	// Session interface
	void addListener(IMessageListener aListener);
	
	void deleteListener(IMessageListener aListener);
	
	void setMessageFilter(IMessageFilter aFilter);
}
