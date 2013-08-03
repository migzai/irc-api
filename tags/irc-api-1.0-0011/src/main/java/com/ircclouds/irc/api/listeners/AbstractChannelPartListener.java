package com.ircclouds.irc.api.listeners;

import java.util.*;

import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;

public abstract class AbstractChannelPartListener
{
	private Map<String, Callback<String>> callbacks = new HashMap<String, Callback<String>>();
		
	public void submit(String aChannelName, Callback<String> aCallback)
	{
		callbacks.put(aChannelName, aCallback);
	}
	
	public void onChannelPart(ChanPartMessage aMsg)
	{
		Callback<String> _callback = callbacks.remove(aMsg.getChannelName());
		if (_callback != null)
		{
			_callback.onSuccess(aMsg.getChannelName());
		}
		
		deleteChannel(aMsg.getChannelName());
	}
	
	public void onServerMessage(ServerNumericMessage aServerMessage)
	{
		if (aServerMessage.getNumericCode() == IRCServerNumerics.NO_SUCH_CHANNEL)
		{
			String _chan = aServerMessage.getText().split(" ")[0];
			if (callbacks.containsKey(_chan))
			{
				callbacks.remove(_chan).onFailure(new IRCException(aServerMessage.getText()));
			}
		}
	}
	
	protected abstract void deleteChannel(String aChannelName);
}
