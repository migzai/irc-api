package com.ircclouds.irc.api.state;

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
			WritableIRCUser _user = aMsg.getSource();
			WritableIRCChannel _chan = getIRCStateImpl().getWritableChannelByName(aMsg.getChannelName());
			
			savedOldState(_chan);
			
			_chan.addUser(_user);
		}
	}

	@Override
	public void onChannelPart(ChanPartMessage aMsg)
	{
		if (!isForMe(aMsg))
		{
			WritableIRCChannel _chan = getIRCStateImpl().getWritableChannelByName(aMsg.getChannelName());
			
			savedOldState(_chan);

			_chan.removeUser(aMsg.getSource());
		}
	}

	@Override
	public void onNickChange(NickMessage aMsg)
	{
		WritableIRCUser _old = new WritableIRCUser(aMsg.getSource().getNick());
		WritableIRCUser _new = new WritableIRCUser(aMsg.getNewNick());
		
		for (WritableIRCChannel _chan : getIRCStateImpl().getChannelsMutable())
		{
			savedOldState(_chan);
			
			if (_chan.getUsers().contains(_old))
			{
				_chan.addUser(_new, _chan.removeUser(_old));
			}
		}
	}

	@Override
	public void onUserQuit(QuitMessage aMsg)
	{
		for (WritableIRCChannel _chan : getIRCStateImpl().getChannelsMutable())
		{
			savedOldState(_chan);
			
			_chan.removeUser(aMsg.getSource());			
		}
	}

	@Override
	public void onTopicChange(TopicMessage aMsg)
	{		
		WritableIRCChannel _chan = getIRCStateImpl().getWritableChannelByName(aMsg.getChannelName());
		
		savedOldState(_chan);
		
		WritableIRCTopic _wit = (WritableIRCTopic) _chan.getTopic();
		_wit.setDate(aMsg.getTopic().getDate());
		_wit.setSetBy(aMsg.getTopic().getSetBy());
		_wit.setValue(aMsg.getTopic().getValue());
	}

	@Override
	public void onChannelKick(ChannelKick aMsg)
	{
		WritableIRCChannel _chan = getIRCStateImpl().getWritableChannelByName(aMsg.getChannelName());
		
		savedOldState(_chan);
		
		_chan.removeUser(new WritableIRCUser(aMsg.getKickedUser()));
	}

	@Override
	public void onChannelMode(ChannelModeMessage aMsg)
	{
		String _chanName = aMsg.getChannelName();
		WritableIRCChannel _chan = getIRCStateImpl().getWritableChannelByName(_chanName);
		
		savedOldState(_chan);
		
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
						_chan.getStatusesForUser(new WritableIRCUser(_usm.getUser())).add(_us);
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
					_chan.getStatusesForUser(new WritableIRCUser(_usm.getUser())).remove(_us);
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
	public void saveChan(WritableIRCChannel aChannel)
	{
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
		for (WritableIRCChannel _chan : getIRCStateImpl().getChannelsMutable())
		{
			if (_chan.getName().equals(aChannel))
			{
				_chan.getUsers().remove(new WritableIRCUser(aNick));
				break;
			}
		}
	}
	
	private void savedOldState(WritableIRCChannel aChan)
	{
		getPreviousIRCStateImpl().getChannelsMutable().remove(aChan);
		getPreviousIRCStateImpl().getChannelsMutable().add(StateUtils.cloneChannel(aChan));
	}
}