package com.ircclouds.irc.api.listeners;

import java.util.*;

import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.utils.*;

public class KickUserListener
{
	private Map<ChanNickTuple, Callback<String>> callbacks = new HashMap<ChanNickTuple, Callback<String>>();
	
	public void onChannelKick(ChannelKick aChanKick)
	{
		if (callbacks.containsKey(aChanKick))
		{
			callbacks.get(aChanKick).onSuccess("");
		}
	}

	public void onServerMessage(ServerMessage aServerMessage)
	{
		if (aServerMessage.getNumericCode() == IRCServerNumerics.NO_SUCH_CHANNEL)
		{
			String _chan = aServerMessage.getText().split(" ")[0];
			if (callbacks.containsKey(_chan))
			{
				callbacks.remove(_chan).onFailure(aServerMessage.getText());
			}
		}
	}

	public void submit(String aChannel, String aNick, Callback<String> aCallback)
	{
		callbacks.put(new ChanNickTuple(aChannel, aNick), aCallback);
	}
	
	private class ChanNickTuple extends Tuple<String, String>
	{
		public ChanNickTuple(String aK, String aV)
		{
			super(aK, aV);
		}		
	}
}
