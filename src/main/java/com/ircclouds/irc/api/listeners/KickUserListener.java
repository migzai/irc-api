package com.ircclouds.irc.api.listeners;

import java.util.*;

import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;

public abstract class KickUserListener
{
	private Map<String, Callback<String>> callbacks = new HashMap<String, Callback<String>>();
	
	public void onChannelKick(ChannelKick aChanKick)
	{
		if (callbacks.containsKey(aChanKick))
		{
			callbacks.get(aChanKick).onSuccess("");
			delChanUser(aChanKick.getChannelName(), aChanKick.getKickedUser());
		}
	}

	public void onServerMessage(ServerMessage aServerMessage)
	{
		if (aServerMessage.getNumericCode() == IRCServerNumerics.NO_SUCH_CHANNEL || aServerMessage.getNumericCode() == IRCServerNumerics.NOT_CHANNEL_OP)
		{
			String _chan = aServerMessage.getText().split(" ")[0];
			if (callbacks.containsKey(_chan))
			{
				callbacks.remove(_chan).onFailure(new IRCException(aServerMessage.getText()));
			}
		}
	}

	public void submit(String aChannel, Callback<String> aCallback)
	{
		callbacks.put(aChannel, aCallback);
	}
	
	protected abstract void delChanUser(String aChan, String aUser);
}
