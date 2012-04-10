package com.ircclouds.irc.api.listeners;

import java.util.*;

import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;

public abstract class AbstractNickChangeListener
{
	private Map<String, Callback<String>> callbacks = new HashMap<String, Callback<String>>();
	
	protected abstract void changeNick(String aNewNick);

	public void submit(String aNewNick, Callback<String> aCallback)
	{
		callbacks.put(aNewNick, aCallback);
	}

	public void onNickChange(NickMessage aMsg)
	{
		Callback<String> _callback = callbacks.get(aMsg.getNewNick());
		if (_callback != null)
		{
			_callback.onSuccess(aMsg.getNewNick());
		}
		else
		{
			changeNick(aMsg.getNewNick());
		}
	}

	public void onServerMessage(ServerMessage aServerMessage)
	{
		Callback<String> _callback = callbacks.remove(aServerMessage.getText().split(" ")[0]);
		if (_callback != null)
		{
			if (aServerMessage.getNumericCode().equals(IRCServerNumerics.NICKNAME_IN_USE))
			{
				_callback.onFailure(aServerMessage.getText());
			}
			else if (aServerMessage.getNumericCode().equals(IRCServerNumerics.ERRONEUS_NICKNAME))
			{
				_callback.onFailure(aServerMessage.getText());
			}
			else if (aServerMessage.getNumericCode().equals(IRCServerNumerics.ERR_NICKTOOFAST))
			{
				_callback.onFailure(aServerMessage.getText());
			}
		}
	}
}
