package com.ircclouds.irc.api.listeners;

import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;
import com.ircclouds.irc.api.state.*;

public abstract class AbstractExecuteCommandListener extends VariousMessageListenerAdapter implements ISaveState
{
	private AbstractChannelJoinListener chanJoinListener;
	private AbstractChannelPartListener chanPartListener;
	private ConnectCmdListener connectListener;
	
	public AbstractExecuteCommandListener(IIRCSession aSession)
	{
		chanJoinListener = new AbstractChannelJoinListener()
		{
			@Override
			public void saveChannel(IRCChannel aChannel)
			{
				save(aChannel);
			}

			@Override
			protected IRCUserStatuses getIRCUserStatuses()
			{
				return getIRCState().getServerOptions().getUserChanStatuses();
			}
		};
		chanPartListener = new AbstractChannelPartListener()
		{
			@Override
			protected void deleteChannel(String aChannelName)
			{
				delete(aChannelName);
			}
		};	
		connectListener = new ConnectCmdListener(aSession);
	}

	@Override
	public void onChannelJoin(ChanJoinMessage aMsg)
	{
		if (isForMe(aMsg))
		{
			chanJoinListener.onChanJoinMessage(aMsg);
		}
	}

	@Override
	public void onChannelPart(ChanPartMessage aMsg)
	{
		if (isForMe(aMsg))
		{
			chanPartListener.onChannelPart(aMsg);
		}
	}

	@Override
	public void onServerMsg(ServerMessage aMsg)
	{
		chanJoinListener.onServerMessage(aMsg);
		chanPartListener.onServerMessage(aMsg);
		connectListener.onMessage(aMsg);
	}
	
	@Override
	public void onError(ErrorMessage aMsg)
	{
		connectListener.onMessage(aMsg);
	}
	
	@Override
	public void onServerPing(ServerPing aMsg)
	{
		connectListener.onMessage(aMsg);
	}
	
	public void submitConnectCallback(Callback<IIRCState> aCallback, IServerParameters aServerParameters)
	{
		connectListener.setCallback(aCallback, aServerParameters);
	}

	public void submitJoinChannelCallback(String aChanName, final Callback<IRCChannel> aCallback)
	{
		chanJoinListener.submit(aChanName, new Callback<IRCChannel>()
		{
			@Override
			public void onSuccess(IRCChannel aChannel)
			{
				save(aChannel);
				aCallback.onSuccess(aChannel);
			}

			@Override
			public void onFailure(String aErrorMessage)
			{
				aCallback.onFailure(aErrorMessage);
			}
		});
	}

	public void submitPartChannelCallback(String aChanName, Callback<String> aCallback)
	{
		chanPartListener.submit(aChanName, aCallback);
	}
	
	private boolean isForMe(IUserMessage aMsg)
	{
		return getIRCState().getNickname().equals(aMsg.getFromUser().getNick());
	}
}
