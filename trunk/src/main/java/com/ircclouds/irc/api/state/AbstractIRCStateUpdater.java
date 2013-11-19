package com.ircclouds.irc.api.state;

import java.util.*;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;
import com.ircclouds.irc.api.listeners.*;
import com.ircclouds.irc.api.utils.*;

public abstract class AbstractIRCStateUpdater extends VariousMessageListenerAdapter implements IStateAccessor
{
	@Override
	public void onChannelJoin(ChanJoinMessage aMsg)
	{
		if (!isForMe(aMsg))
		{
			IRCUser _user = aMsg.getSource();
			IRCChannel _chan = getIRCStateImpl().getChannelByNameMutable(aMsg.getChannelName());
			
			savedOldState(_chan);
			
			_chan.addUser(_user, new HashSet<IRCUserStatus>());
		}
	}

	@Override
	public void onChannelPart(ChanPartMessage aMsg)
	{
		if (!isForMe(aMsg))
		{
			IRCChannel _chan = getIRCStateImpl().getChannelByNameMutable(aMsg.getChannelName());
			
			savedOldState(_chan);

			_chan.removeUser(aMsg.getSource());
		}
	}

	@Override
	public void onNickChange(NickMessage aMsg)
	{
		IRCUser _old = new IRCUser(aMsg.getSource().getNick());
		IRCUser _new = new IRCUser(aMsg.getNewNick());
		
		for (IRCChannel _chan : getIRCStateImpl().getChannelsMutable())
		{
			savedOldState(_chan);
			
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
			savedOldState(_chan);
			
			_chan.removeUser(aMsg.getSource());			
		}
	}

	@Override
	public void onTopicChange(TopicMessage aMsg)
	{		
		IRCChannel _chan = getIRCStateImpl().getChannelByNameMutable(aMsg.getChannelName());
		
		savedOldState(_chan);
		
		_chan.setTopic(aMsg.getTopic());	
	}

	@Override
	public void onChannelKick(ChannelKick aMsg)
	{
		IRCChannel _chan = getIRCStateImpl().getChannelByNameMutable(aMsg.getChannelName());
		
		savedOldState(_chan);
		
		_chan.removeUser(new IRCUser(aMsg.getKickedUser()));			
	}

	@Override
	public void onChannelMode(ChannelModeMessage aMsg)
	{
		String _chanName = aMsg.getChannelName();
		IRCChannel _chan = getIRCStateImpl().getChannelByNameMutable(_chanName);
		
		savedOldState(_chan);
		
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
	
	private IRCStateImpl getPreviousIRCStateImpl()
	{
		return (IRCStateImpl) getIRCStateImpl().getPrevious();
	}
	
	private IRCUserStatuses getAvailableUserStatuses()
	{
		return getIRCState().getServerOptions().getUserChanStatuses();
	}
	
	private boolean isForMe(IUserMessage aMsg)
	{
		return getIRCState().getNickname().equals(aMsg.getSource().getNick());
	}

	@Override
	public void saveChan(IRCChannel aChannel)
	{
		System.out.println("saving");
		getIRCStateImpl().getChannelsMutable().add(aChannel);
	}

	@Override
	public void deleteChan(String aChannelName)
	{
		getIRCStateImpl().getChannelsMutable().remove(aChannelName);
	}
	
	@Override
	public void updateNick(String aNewNick)
	{
		getIRCStateImpl().updateNick(aNewNick);
	}
	
	@Override
	public void deleteNickFromChan(String aChannel, String aNick)
	{
		for (IRCChannel _chan : getIRCStateImpl().getChannelsMutable())
		{
			if (_chan.getName().equals(aChannel))
			{
				_chan.getUsers().remove(new IRCUser(aNick));
				break;
			}
		}
	}
	
	private void savedOldState(IRCChannel aChan)
	{
		getPreviousIRCStateImpl().getChannelsMutable().remove(aChan);
		getPreviousIRCStateImpl().getChannelsMutable().add(StateUtils.cloneChannel(aChan));
	}
}