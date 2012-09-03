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
	private AbstractNickChangeListener nickChangeListener;
	private KickUserListener kickUserListener;
	private AsyncMessageListener messsageListener;
	
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
		nickChangeListener = new AbstractNickChangeListener()
		{
			@Override
			protected void changeNick(String aNewNick)
			{
				updateNick(aNewNick);
			}
		};
		kickUserListener = new KickUserListener();
		messsageListener = new AsyncMessageListener();
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
	public void onChannelKick(ChannelKick aChanKick)
	{
		if (isForMe(aChanKick))
		{
			kickUserListener.onChannelKick(aChanKick);
		}
	}
	
	@Override
	public void onServerMsg(ServerMessage aMsg)
	{
		chanJoinListener.onServerMessage(aMsg);
		chanPartListener.onServerMessage(aMsg);
		if (!getIRCState().isConnected())
		{
			connectListener.onMessage(aMsg);
		}
		nickChangeListener.onServerMessage(aMsg);
		messsageListener.onServerMsg(aMsg);
		kickUserListener.onServerMsg(aMsg);
	}
	
	@Override
	public void onError(ErrorMessage aMsg)
	{
		if (!getIRCState().isConnected())
		{
			connectListener.onMessage(aMsg);
		}
	}
	
	@Override
	public void onNickChange(NickMessage aMsg)
	{
		if (aMsg.getFromUser().getNick().equals(getIRCState().getNickname()))
		{
			nickChangeListener.onNickChange(aMsg);
			
			updateNick(aMsg.getNewNick());
		}
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

	public void submitChangeNickCallback(String aNewNickname, Callback<String> aCallback)
	{
		nickChangeListener.submit(aNewNickname, aCallback);
	}

	public void submitSendMessageCallback(int aAsyncId, Callback<String> aCallback)
	{
		messsageListener.submit(aAsyncId, aCallback);
	}
	
	public void submitKickUserCallback(String aChannel, String aNick, Callback<String> aCallback)
	{
		kickUserListener.submit(aChannel, aNick, aCallback);
	}
	
	private boolean isForMe(IUserMessage aMsg)
	{
		return getIRCState().getNickname().equals(aMsg.getFromUser().getNick());
	}
}
