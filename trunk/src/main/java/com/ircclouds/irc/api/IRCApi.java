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

	void message(String aTarget, String aText);

	void message(String aTarget, String aMessage, Callback<String> aCallback);
	
	void act(String aTarget, String aMessage);

	void act(String aTarget, String aMessage, Callback<String> aCallback);
	
	void notice(String aNick, String aText);
	
	void notice(String aNick, String aText, Callback<String> aCallback);

	void kick(String aChannel, String aNick);
	
	void kick(String aChannel, String aNick, String aKickMessage);

	void kick(String aChannel, String aNick, Callback<String> aCallback);
	
	void kick(String aChannel, String aNick, String aKickMessage, Callback<String> aCallback);
	
	void changeTopic(String aChannel, String aTopic);
	
	void changeMode(String aModeString);
	
	void rawMessage(String aMessage);
	
	// DCC cmds
	void dccSend(String aNick, File aFile, DCCSendCallback aCallback);

	void dccSend(String aNick, File aFile, Integer aTimeout, DCCSendCallback aCallback);
	
	void dccSend(String aNick, Integer aListeningPort, File aFile, DCCSendCallback aCallback);
	
	void dccSend(String aNick, File aFile, Integer aListeningPort, Integer aTimeout, DCCSendCallback aCallback);
	
	void dccAccept(String aNick, File aFile, Integer aPort, Integer aResumePosition, DCCSendCallback aCallback);
	
	void dccAccept(String aNick, File aFile, Integer aPort, Integer aResumePosition, Integer aTimeout, DCCSendCallback aCallback);
	
	void dccReceive(File aFile, Integer aSize, SocketAddress aAddress, DCCReceiveCallback aCallback);
	
	void dccResume(File aFile, Integer aResumePosition, Integer aSize, SocketAddress aAddress, DCCReceiveCallback aCallback);
	
	DCCManager getDCCManager();
	
	// Session interface
	void addListener(IMessageListener aListener);
	
	void deleteListener(IMessageListener aListener);
	
	void setMessageFilter(IMessageFilter aFilter);
}
