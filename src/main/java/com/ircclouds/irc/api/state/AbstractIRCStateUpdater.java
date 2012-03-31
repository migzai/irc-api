package com.ircclouds.irc.api.state;

import java.util.*;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;
import com.ircclouds.irc.api.listeners.*;

public abstract class AbstractIRCStateUpdater extends VariousMessageListenerAdapter implements ISaveState
{
	@Override
	public void onChannelJoin(ChanJoinMessage aMsg)
	{
		if (!isForMe(aMsg))
		{
			IRCUser _user = aMsg.getFromUser();
			IRCChannel _chan = getIRCStateImpl().getChannelByNameMutable(aMsg.getChannelName());
			_chan.addUser(_user, new HashSet<IRCUserStatus>());
		}
	}

	@Override
	public void onChannelPart(ChanPartMessage aMsg)
	{
		if (!isForMe(aMsg))
		{
			getIRCStateImpl().getChannelByNameMutable(aMsg.getChannelName()).removeUser(aMsg.getFromUser());
		}
	}

	@Override
	public void onNickChange(NickMessage aMsg)
	{
		IRCUser _old = new IRCUser(aMsg.getFromUser().getNick());
		IRCUser _new = new IRCUser(aMsg.getNewNick());

		for (IRCChannel _chan : getIRCStateImpl().getChannelsMutable())
		{
			if (_chan.getUsers().containsKey(_old))
			{
				_chan.addUser(_new, _chan.removeUser(_old));
			}
		}
	}

	@Override
	public void onUserQuit(QuitMessage aMsg)
	{
		for (IRCChannel _chan : getIRCStateImpl().getChannelsMutable())
		{
			_chan.removeUser(aMsg.getFromUser());
		}
	}

	@Override
	public void onTopicChange(TopicMessage aMsg)
	{
		getIRCStateImpl().getChannelByNameMutable(aMsg.getChannelName()).setTopic(aMsg.getTopic());
	}

	@Override
	public void onChannelKickMessage(ChannelKickMsg aMsg)
	{
		getIRCStateImpl().getChannelByNameMutable(aMsg.getChannelName()).removeUser(new IRCUser(aMsg.getKickedUser()));
	}

	@Override
	public void onChannelMode(ChannelModeMessage aMsg)
	{
		String _chanName = aMsg.getChannelName();
		IRCChannel _chan = getIRCStateImpl().getChannelByNameMutable(_chanName);

		Map<IRCUser, Set<IRCUserStatus>> _users = _chan.getUsers();
		for (ChannelMode _mode : aMsg.getAddedModes())
		{
			if (_mode instanceof IRCUserStatusMode)
			{
				if (aMsg.getRemovedModes().contains(_mode))
				{
					aMsg.getRemovedModes().remove(_mode);
				}
				else
				{
					IRCUserStatusMode _usm = (IRCUserStatusMode) _mode;
					IRCUserStatus _us = getAvailableUserStatuses().getUserStatus(_usm.getChannelModeType());
					if (_us != null)
					{
						_users.get(new IRCUser(_usm.getUser())).add(_us);
					}
				}
			}
		}
		for (ChannelMode _mode : aMsg.getRemovedModes())
		{
			if (_mode instanceof IRCUserStatusMode)
			{
				IRCUserStatusMode _usm = (IRCUserStatusMode) _mode;
				IRCUserStatus _us = getAvailableUserStatuses().getUserStatus(_usm.getChannelModeType());
				if (_us != null)
				{
					_users.get(new IRCUser(_usm.getUser())).remove(_us);
				}
			}
		}
	}

	private IRCStateImpl getIRCStateImpl()
	{
		return (IRCStateImpl) getIRCState();
	}
	
	private IRCUserStatuses getAvailableUserStatuses()
	{
		return getIRCState().getServerOptions().getUserChanStatuses();
	}
	
	private boolean isForMe(IUserMessage aMsg)
	{
		return getIRCState().getNickname().equals(aMsg.getFromUser().getNick());
	}

	@Override
	public void save(IRCChannel aChannel)
	{
		getIRCStateImpl().getChannelsMutable().add(aChannel);
	}

	@Override
	public void delete(String aChannelName)
	{
		getIRCStateImpl().getChannelsMutable().remove(aChannelName);
	}
}
