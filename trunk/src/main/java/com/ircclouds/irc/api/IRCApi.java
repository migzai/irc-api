package com.ircclouds.irc.api;

import java.io.*;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.filters.*;
import com.ircclouds.irc.api.listeners.*;
import com.ircclouds.irc.api.state.*;

public interface IRCApi
{	
	// Cmds Interface
	void connect(IServerParameters aServerParameters, Callback<IIRCState> aCallback) throws IOException;
	
	void disconnect(String aQuitMessage) throws IOException;

	void joinChannel(String aChannelName) throws IOException;

	void joinChannelAsync(String aChannelName, Callback<IRCChannel> aCallback) throws IOException;

	void joinChannel(String aChannelName, String aKey) throws IOException;
	
	void joinChannelAsync(String aChannelName, String aKey, Callback<IRCChannel> aCallback) throws IOException;

	void leaveChannel(String aChannelName) throws IOException;

	void leaveChannelAsync(String aChannelName, Callback<String> aCallback) throws IOException;

	void leaveChannel(String aChannelName, String aPartMessage) throws IOException;

	void leaveChannelAsync(String aChannelName, String aPartMessage, Callback<String> aCallback) throws IOException;

	void changeMode(String aModeString) throws IOException;
	
	void changeNick(String aNewNick) throws IOException;
	
	void changeNickAsync(String aNewNick, Callback<String> aCallback) throws IOException;	
	
	void changeTopic(String aChannel, String aTopic) throws IOException;
	
	void sendChannelMessage(String aChannelName, String aMessage) throws IOException;

	void sendChannelMessageAsync(String aChannelName, String aMessage, Callback<String> aCallback) throws IOException;
	
	void sendPrivateMessage(String aNick, String aText) throws IOException;
	
	void actInChannel(String aChannelName, String aActionMessage) throws IOException;

	void actInPrivate(String aNick, String aActionMessage) throws IOException;
	
	void sendRawMessage(String aMessage) throws IOException;
	
	// Session interface
	void addListener(IMessageListener aListener);
	
	void deleteListener(IMessageListener aListener);
	
	void setMessageFilter(IMessageFilter aFilter);
}
