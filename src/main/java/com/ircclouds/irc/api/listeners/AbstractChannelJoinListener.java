package com.ircclouds.irc.api.listeners;

import java.util.*;

import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.utils.*;

public abstract class AbstractChannelJoinListener
{
	private Map<String, Callback<IRCChannel>> callbacks = new HashMap<String, Callback<IRCChannel>>();

	private WritableIRCChannel channel;
	private WritableIRCTopic topic;

	public void submit(String aChannelName, Callback<IRCChannel> aCallback)
	{
		callbacks.put(aChannelName, aCallback);
	}

	public void onChanJoinMessage(ChanJoinMessage aMsg)
	{
		saveChannel(channel = new WritableIRCChannel(aMsg.getChannelName()));
	}

	public void onServerMessage(ServerNumericMessage aServerMessage)
	{
		int _numcode = aServerMessage.getNumericCode();
		if (_numcode == IRCServerNumerics.CHANNEL_FORWARD || _numcode == IRCServerNumerics.TOPIC_USER_DATE || _numcode == IRCServerNumerics.CHANNEL_NICKS_LIST
				|| _numcode == IRCServerNumerics.CHANNEL_TOPIC || _numcode == IRCServerNumerics.CHANNEL_NICKS_END_OF_LIST
				|| _numcode == IRCServerNumerics.CHANNEL_CANNOT_JOIN_INVITE || _numcode == IRCServerNumerics.CHANNEL_CANNOT_JOIN_KEYED
				|| _numcode == IRCServerNumerics.CHANNEL_CANNOT_JOIN_FULL)
		{
			if (channel != null)
			{
				if (_numcode == IRCServerNumerics.CHANNEL_NICKS_LIST)
				{
					String _nicks[] = aServerMessage.getText().substring(aServerMessage.getText().indexOf(":") + 1).split(" ");
					for (String _nick : _nicks)
					{
						add(_nick);
					}
				}
				else if (_numcode == IRCServerNumerics.CHANNEL_TOPIC)
				{
					topic = new WritableIRCTopic(getTopic(aServerMessage));
				}
				else if (_numcode == IRCServerNumerics.TOPIC_USER_DATE)
				{
					String _cmpnts[] = aServerMessage.getText().split(" ");
					topic.setSetBy(_cmpnts[1]);
					topic.setDate(new Date(Long.parseLong(_cmpnts[2] + "000")));
					channel.setTopic(topic);
				}
				else if (_numcode == IRCServerNumerics.CHANNEL_NICKS_END_OF_LIST)
				{
					Callback<IRCChannel> _chanCallback = callbacks.remove(channel.getName());
					if (_chanCallback != null)
					{
						_chanCallback.onSuccess(channel);
					}
					channel = null;
					topic = null;
				}
			}
			else if (callbacks.containsKey(getChannelNameFrom(aServerMessage.getText())))
			{
				if (_numcode == IRCServerNumerics.CHANNEL_CANNOT_JOIN_INVITE)
				{
					callbacks.remove(getChannelNameFrom(aServerMessage.getText())).onFailure(new IRCException(aServerMessage.getText()));
				}
				else if (_numcode == IRCServerNumerics.CHANNEL_CANNOT_JOIN_KEYED)
				{
					callbacks.remove(getChannelNameFrom(aServerMessage.getText())).onFailure(new IRCException(aServerMessage.getText()));
				}
				else if (_numcode == IRCServerNumerics.CHANNEL_CANNOT_JOIN_BANNED)
				{
					callbacks.remove(getChannelNameFrom(aServerMessage.getText())).onFailure(new IRCException(aServerMessage.getText()));
				}
				else if (_numcode == IRCServerNumerics.CHANNEL_CANNOT_JOIN_FULL)
				{
					callbacks.remove(getChannelNameFrom(aServerMessage.getText())).onFailure(new IRCException(aServerMessage.getText()));
				}
				else if (_numcode == IRCServerNumerics.CHANNEL_FORWARD)
				{
					String[] _components = aServerMessage.getText().split(" ");
					Callback<IRCChannel> callback = callbacks.remove(_components[0]);
					if (callback != null)
					{
						callbacks.put(_components[1], callback);
					}
				}					
			}		
		}
	}

	protected abstract void saveChannel(WritableIRCChannel aChannel);

	protected abstract IRCUserStatuses getIRCUserStatuses();

	private String getChannelNameFrom(String aMessage)
	{
		return aMessage.split(" ")[0];
	}

	private void add(String aNick)
	{
		boolean _flag = true;

		char _pref = aNick.charAt(0);
		for (final IRCUserStatus _ucs : getIRCUserStatuses())
		{
			if (_ucs.getPrefix().equals(_pref))
			{
				channel.addUser(new WritableIRCUser(aNick.substring(1)), new SynchronizedUnmodifiableSet<IRCUserStatus>(new HashSet<IRCUserStatus>()
				{
					{
						add(_ucs);
					}
				}));
				_flag = false;
				break;
			}
		}

		if (_flag)
		{
			channel.addUser(new WritableIRCUser(aNick));
		}
	}

	private String getTopic(ServerNumericMessage aServMsg)
	{
		return aServMsg.getText().substring(aServMsg.getText().indexOf(":") + 1);
	}
}
