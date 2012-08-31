package com.ircclouds.irc.api;

import static com.ircclouds.irc.api.DCCManagerImpl.*;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.log4j.*;
import org.slf4j.*;
import org.slf4j.Logger;

import com.ircclouds.irc.api.commands.*;
import com.ircclouds.irc.api.ctcp.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.filters.*;
import com.ircclouds.irc.api.listeners.*;
import com.ircclouds.irc.api.state.*;
import com.ircclouds.irc.api.utils.*;

public class IRCApiImpl implements IRCApi
{
	private static final Logger LOG = LoggerFactory.getLogger(IRCApiImpl.class);

	private IIRCSession session;
	private AbstractExecuteCommandListener executeCmdListener;
	private IIRCState state;
	private int asyncId = 0;

	private IMessageFilter filter;
	private ApiMessageFilter apiFilter = new ApiMessageFilter(asyncId);
	private IMessageFilter abstractAndMsgFilter = new AbstractAndMessageFilter(apiFilter)
	{
		@Override
		protected IMessageFilter getSecondFilter()
		{
			return filter;
		}
	};
	
	private DCCManagerImpl dccManager;

	public IRCApiImpl(Boolean aSaveIRCState)
	{
		configLog4j();

		state = new DisconnectedIRCState();
		session = new AbstractIRCSession()
		{
			@Override
			protected IRCServerOptions getIRCServerOptions()
			{
				return state.getServerOptions();
			}

			@Override
			public IMessageFilter getMessageFilter()
			{
				if (filter != null)
				{
					return abstractAndMsgFilter;
				}
				else
				{
					return apiFilter;
				}
			}
		};

		session.addListeners(ListenerLevel.PRIVATE, executeCmdListener = new ExecuteCommandListenerImpl(session, getStateUpdater(aSaveIRCState)), new PingVersionListenerImpl(
				session));
		
		dccManager = new DCCManagerImpl(this);
	}

	@Override
	public void connect(final IServerParameters aServerParameters, final Callback<IIRCState> aCallback)
	{
		if (state.isConnected())
		{
			throw new ApiException("Already connected!");
		}

		executeCmdListener.submitConnectCallback(newConnectCallback(aCallback), aServerParameters);

		boolean _isOpen = false;
		try
		{
			if (_isOpen = session.open(aServerParameters.getServer()))
			{
				execute(new ConnectCmd(aServerParameters));
			}
			else
			{
				aCallback.onFailure("Failed to open connection to [" + aServerParameters.getServer().toString() + "]");
			}
		}
		catch (IOException aExc)
		{
			LOG.error("Error opening session", aExc);
			throw new ApiException(aExc);
		}
		finally
		{
			if (!_isOpen)
			{
				closeSession();
			}
		}
	}

	@Override
	public void disconnect(String aQuitMessage)
	{
		checkConnected();

		execute(new QuitCmd(aQuitMessage));

		((IRCStateImpl) (state)).setConnected(false);
	}

	@Override
	public void joinChannel(String aChannelName)
	{
		joinChannel(aChannelName, "");
	}

	@Override
	public void joinChannel(String aChannelName, Callback<IRCChannel> aCallback)
	{
		joinChannel(aChannelName, "", aCallback);
	}

	@Override
	public void joinChannel(String aChannelName, String aKey)
	{
		checkConnected();

		execute(new JoinChanCmd(prependChanType(aChannelName), aKey));
	}

	@Override
	public void joinChannel(String aChannelName, String aKey, final Callback<IRCChannel> aCallback)
	{
		checkConnected();

		aChannelName = prependChanType(aChannelName);

		executeCmdListener.submitJoinChannelCallback(aChannelName, aCallback);
		execute(new JoinChanCmd(aChannelName, aKey));
	}

	@Override
	public void leaveChannel(String aChannelName)
	{
		leaveChannel(aChannelName, "");
	}

	@Override
	public void leaveChannel(String aChannelName, Callback<String> aCallback)
	{
		leaveChannel(aChannelName, "", aCallback);
	}

	@Override
	public void leaveChannel(String aChannelName, String aPartMessage)
	{
		checkConnected();

		execute(new PartChanCmd(aChannelName, aPartMessage));
	}

	@Override
	public void leaveChannel(String aChannelName, String aPartMessage, Callback<String> aCallback)
	{
		checkConnected();

		executeCmdListener.submitPartChannelCallback(aChannelName, aCallback);
		execute(new PartChanCmd(aChannelName, aPartMessage));
	}

	@Override
	public void message(String aTarget, String aMessage)
	{
		checkConnected();

		execute(new SendPrivateMessage(aTarget, aMessage));
	}

	@Override
	public void message(String aTarget, String aMessage, Callback<String> aCallback)
	{
		checkConnected();

		executeCmdListener.submitSendMessageCallback(asyncId, aCallback);
		apiFilter.addValue(asyncId);
		
		execute(new SendPrivateMessage(aTarget, aMessage, asyncId++));
	}

	@Override
	public void act(String aTarget, String aActionMessage)
	{
		checkConnected();
		
		execute(new SendActionMessage(aTarget, aActionMessage));
	}

	@Override
	public void act(String aTarget, String aActionMessage, Callback<String> aCallback)
	{
		checkConnected();

		executeCmdListener.submitSendMessageCallback(asyncId, aCallback);
		apiFilter.addValue(asyncId);

		execute(new SendActionMessage(aTarget, aActionMessage, asyncId++));
	}
	
	@Override
	public void notice(String aTarget, String aText)
	{
		checkConnected();
		
		execute(new SendNoticeMessage(aTarget, aText));
	}

	@Override
	public void notice(String aTarget, String aText, Callback<String> aCallback)
	{
		checkConnected();
		
		executeCmdListener.submitSendMessageCallback(asyncId, aCallback);
		apiFilter.addValue(asyncId);
		
		execute(new SendNoticeMessage(aTarget, aText, asyncId++));
	}
	
	@Override
	public void changeNick(String aNewNick)
	{
		checkConnected();

		execute(new ChangeNickCmd(aNewNick));
	}

	@Override
	public void changeNick(String aNewNickname, Callback<String> aCallback)
	{
		checkConnected();

		executeCmdListener.submitChangeNickCallback(aNewNickname, aCallback);
		apiFilter.addValue(asyncId);

		execute(new ChangeNickCmd(aNewNickname));
	}

	@Override
	public void changeTopic(final String aChannel, final String aSuggestedTopic)
	{
		checkConnected();

		execute(new ChangeTopicCmd(aChannel, aSuggestedTopic));
	}

	@Override
	public void changeMode(String aModeString)
	{
		checkConnected();

		execute(new ChangeModeCmd(aModeString));
	}

	@Override
	public void sendRawMessage(String aMessage)
	{
		execute(new SendRawMessage(aMessage));
	}

	@Override
	public void addListener(IMessageListener aListener)
	{
		session.addListeners(ListenerLevel.PUBLIC, aListener);
	}

	@Override
	public void deleteListener(IMessageListener aListener)
	{
		session.removeListener(aListener);
	}

	@Override
	public void setMessageFilter(IMessageFilter aFilter)
	{
		filter = aFilter;
	}

	@Override
	public void dccSend(final String aNick, final File aFile, DCCSendCallback aCallback)
	{
		dccSend(aNick, aFile, NetUtils.getRandDccPort(), DCC_SEND_TIMEOUT, aCallback);
	}

	@Override
	public void dccSend(String aNick, Integer aListeningPort, File aFile, DCCSendCallback aCallback)
	{
		dccSend(aNick, aFile, aListeningPort, DCC_SEND_TIMEOUT, aCallback);
	}

	@Override
	public void dccSend(String aNick, File aFile, Integer aTimeout, DCCSendCallback aCallback)
	{
		dccSend(aNick, aFile, NetUtils.getRandDccPort(), aTimeout, aCallback);
	}

	@Override
	public void dccSend(String aNick, File aFile, Integer aListeningPort, Integer aTimeout, DCCSendCallback aCallback)
	{
		dccManager.dccSend(aNick, aFile, aListeningPort, aTimeout, aCallback);
	}

	@Override
	public void dccAccept(String aNick, File aFile, Integer aPort, Integer aResumePosition, DCCSendCallback aCallback)
	{
		dccAccept(aNick, aFile, aPort, aResumePosition, DCC_SEND_TIMEOUT, aCallback);
	}

	@Override
	public void dccAccept(String aNick, File aFile, Integer aPort, Integer aResumePosition, Integer aTimeout, DCCSendCallback aCallback)
	{
		dccManager.dccAccept(aNick, aFile, aPort, aResumePosition, aTimeout, aCallback);
	}

	@Override
	public void dccReceive(File aFile, Integer aSize, SocketAddress aAddress, DCCReceiveCallback aCallback)
	{
		dccResume(aFile, 0, aSize, aAddress, aCallback);
	}

	@Override
	public void dccResume(File aFile, Integer aResumePosition, Integer aSize, SocketAddress aAddress, DCCReceiveCallback aCallback)
	{
		dccManager.dccResume(aFile, aResumePosition, aSize, aAddress, aCallback);
	}	
	
	@Override
	public DCCManager getDCCManager()
	{
		return dccManager;
	}
	
	protected ICommandServer getCommandServer()
	{
		return session.getCommandServer();
	}

	private String prependChanType(String aChannelName)
	{
		for (Character _c : state.getServerOptions().getChanTypes())
		{
			if (_c.equals(aChannelName.charAt(0)))
			{
				return aChannelName;
			}
		}

		return state.getServerOptions().getChanTypes().iterator().next() + aChannelName;
	}

	private void closeSession()
	{
		try
		{
			session.close();
		}
		catch (IOException aExc)
		{
			LOG.error("Error Closing Session.", aExc);
		}
	}

	private void checkConnected()
	{
		if (!state.isConnected())
		{
			throw new ApiException("Not connected!");
		}
	}

	private Callback<IIRCState> newConnectCallback(final Callback<IIRCState> aCallback)
	{
		return new Callback<IIRCState>()
		{
			@Override
			public void onSuccess(IIRCState aConnectedState)
			{
				state = aConnectedState;

				((IRCStateImpl) (state)).setConnected(true);

				aCallback.onSuccess(aConnectedState);
			}

			@Override
			public void onFailure(String aErrorMessage)
			{
				aCallback.onFailure(aErrorMessage);
			}
		};
	}

	private void configLog4j()
	{
		PropertyConfigurator.configure(getLog4jProperties());
	}

	private Properties getLog4jProperties()
	{
		Properties _p = new Properties();
		_p.put("log4j.rootLogger", "DEBUG, A1");
		_p.put("log4j.appender.A1.layout", "org.apache.log4j.PatternLayout");
		_p.put("log4j.appender.A1", "org.apache.log4j.ConsoleAppender");
		_p.put("log4j.appender.A1.layout.ConversionPattern", "%d [%t] %p %c{1} - %m%n");
		_p.put("log4j.logger.org.eclipse", "WARN");
		return _p;
	}

	private void execute(ICommand aCommand)
	{
		try
		{
			getCommandServer().execute(aCommand);
		}
		catch (IOException aExc)
		{
			LOG.error("Error executing command", aExc);
			throw new RuntimeException(aExc);
		}
	}

	private ISaveState getStateUpdater(Boolean aSaveIRCState)
	{
		ISaveState _stateUpdater = new AbstractIRCStateUpdater()
		{
			@Override
			public IIRCState getIRCState()
			{
				return state;
			}
		};

		if (aSaveIRCState)
		{
			session.addListeners(ListenerLevel.PRIVATE, (AbstractIRCStateUpdater) _stateUpdater);
		}
		else
		{
			_stateUpdater = new ISaveState()
			{
				@Override
				public void save(IRCChannel aChannel)
				{
					// NOP
				}

				@Override
				public IIRCState getIRCState()
				{
					return state;
				}

				@Override
				public void delete(String aChannelName)
				{
					// NOP
				}

				@Override
				public void updateNick(String aNewNick)
				{
					// NOP
				}
			};
		}

		return _stateUpdater;
	}
}
