package com.ircclouds.irc.api.listeners;

import java.util.*;

import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;

public abstract class AbstractChannelJoinListener
{
	private Map<String, Callback<IRCChannel>> callbacks = new HashMap<String, Callback<IRCChannel>>();
	
	private IRCChannel channel;
	private IRCTopic topic;

	public void submit(String aChannelName, Callback<IRCChannel> aCallback)
	{
		callbacks.put(aChannelName, aCallback);
	}

	public void onChanJoinMessage(ChanJoinMessage aMsg)
	{
		channel = new IRCChannel(aMsg.getChannelName());
	}

	public void onServerMessage(ServerNumericMessage aServerMessage)
	{
		int _numcode = aServerMessage.getNumericCode();
		if (_numcode == IRCServerNumerics.CHANNEL_FORWARD || _numcode == IRCServerNumerics.TOPIC_USER_DATE || _numcode == IRCServerNumerics.CHANNEL_NICKS_LIST
				|| _numcode == IRCServerNumerics.CHANNEL_TOPIC || _numcode == IRCServerNumerics.CHANNEL_NICKS_END_OF_LIST || _numcode == IRCServerNumerics.CHANNEL_CANNOT_JOIN_INVITE
				|| _numcode == IRCServerNumerics.CHANNEL_CANNOT_JOIN_KEYED)
		{
			if (_numcode == IRCServerNumerics.CHANNEL_FORWARD)
			{
				String _newName = aServerMessage.getText().split(" ")[1];
				if (channel != null)
				{
					channel.setName(_newName);
				}
			}
			else if (_numcode == IRCServerNumerics.CHANNEL_NICKS_LIST)
			{
				String _nicks[] = aServerMessage.getText().substring(aServerMessage.getText().indexOf(":") + 1).split(" ");
				for (String _nick : _nicks)
				{
					add(_nick);
				}
			}
			else if (_numcode == IRCServerNumerics.CHANNEL_TOPIC)
			{
				topic = new IRCTopic(getTopic(aServerMessage));
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
				if (channel.getTopic() == null)
				{
					channel.setTopic(new IRCTopic(""));
				}

				Callback<IRCChannel> _chanCallback = callbacks.remove(channel.getName());
				if (_chanCallback != null)
				{
					_chanCallback.onSuccess(channel);
				}
				else
				{
					saveChannel(channel);
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
			}
		}
	}

	protected abstract void saveChannel(IRCChannel aChannel);
	
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
				channel.addUser(new IRCUser(aNick.substring(1)), new HashSet<IRCUserStatus>()
				{
					{
						add(_ucs);
					}
				});
				_flag = false;
				break;
			}
		}

		if (_flag)
		{
			IRCUser _user = new IRCUser();
			_user.setNick(aNick);
			channel.addUser(_user, new HashSet<IRCUserStatus>());
		}
	}

	private String getTopic(ServerNumericMessage aServMsg)
	{
		return aServMsg.getText().substring(aServMsg.getText().indexOf(":") + 1);
	}
}
