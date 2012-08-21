package com.ircclouds.irc.api;

import java.io.*;
import java.math.*;
import java.net.*;
import java.util.*;

import org.apache.log4j.*;
import org.slf4j.*;
import org.slf4j.Logger;

import com.ircclouds.irc.api.commands.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.filters.*;
import com.ircclouds.irc.api.listeners.*;
import com.ircclouds.irc.api.state.*;
import com.ircclouds.irc.api.utils.*;

public class IRCApiImpl implements IRCApi
{
	private static final Logger LOG = LoggerFactory.getLogger(IRCApiImpl.class);

	private static final int DCC_SEND_TIMEOUT = 10000;
	
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
	public void channelMessage(String aChannelName, String aMessage)
	{
		checkConnected();

		execute(new SendChannelMessage(aChannelName, aMessage));
	}

	@Override
	public void channelMessage(String aChannelName, String aMessage, Callback<String> aCallback)
	{
		checkConnected();

		executeCmdListener.submitSendMessageCallback(asyncId, aCallback);
		apiFilter.addValue(asyncId);

		execute(new SendChannelMessage(aChannelName, aMessage, asyncId++));
	}

	@Override
	public void privateMessage(String aNick, String aText)
	{
		checkConnected();

		execute(new SendPrivateMessage(aNick, aText));
	}

	@Override
	public void privateMessage(String aNick, String aText, Callback<String> aCallback)
	{
		checkConnected();

		executeCmdListener.submitSendMessageCallback(asyncId, aCallback);
		apiFilter.addValue(asyncId);

		execute(new SendPrivateMessage(aNick, aText, asyncId++));
	}

	@Override
	public void actInChannel(String aChannelName, String aActionMessage)
	{
		checkConnected();

		execute(new SendChannelActionMessage(aChannelName, aActionMessage));
	}

	@Override
	public void actInChannel(String aChannelName, String aActionMessage, Callback<String> aCallback)
	{
		checkConnected();

		executeCmdListener.submitSendMessageCallback(asyncId, aCallback);
		apiFilter.addValue(asyncId);

		execute(new SendChannelActionMessage(aChannelName, aActionMessage, asyncId++));
	}

	@Override
	public void actInPrivate(String aNick, String aActionMessage)
	{
		checkConnected();

		execute(new SendPrivateActionMessage(aNick, aActionMessage));
	}

	@Override
	public void actInPrivate(String aNick, String aActionMessage, Callback<String> aCallback)
	{
		checkConnected();

		executeCmdListener.submitSendMessageCallback(asyncId, aCallback);
		execute(new SendPrivateActionMessage(aNick, aActionMessage, asyncId++));
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
	public void dccSend(final String aNick, final File aFile)
	{
		dccSend(aNick, aFile, NetUtils.getRandDccPort(), DCC_SEND_TIMEOUT);
	}

	@Override
	public void dccSend(String aNick, Integer aListeningPort, File aFile)
	{
		dccSend(aNick, aFile, aListeningPort, DCC_SEND_TIMEOUT);
	}

	@Override
	public void dccSend(String aNick, File aFile, Integer aTimeout)
	{
		dccSend(aNick, aFile, NetUtils.getRandDccPort(), aTimeout);
	}

	@Override
	public void dccSend(String aNick, File aFile, Integer aListeningPort, Integer aTimeout)
	{
		new Thread(new DCCSendListener(aFile, aTimeout, aListeningPort)).start();
		
		privateMessage(aNick, '\001' + "DCC SEND " + aFile.getName() + " " + getLocalAddressRepresentation() + " " + aListeningPort +  " " + aFile.length() + '\001');
	}

	@Override
	public void dccAccept(String aNick, File aFile, Integer aPort, Integer aResumePosition)
	{
		dccAccept(aNick, aFile, aPort, aResumePosition, DCC_SEND_TIMEOUT);
	}
	
	@Override
	public void dccAccept(String aNick, File aFile, Integer aPort, Integer aResumePosition, Integer aTimeout) 
	{
		new Thread(new DCCSendListener(aFile, aTimeout, aPort, aResumePosition)).start();
		
		privateMessage(aNick, '\001' + "DCC ACCEPT " + aFile.getName() + " " + aPort + " " + aResumePosition + '\001');		
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
	
	private String getLocalAddressRepresentation()
	{
		try 
		{
			InetAddress _localHost = InetAddress.getLocalHost();
			byte[] _address = _localHost.getAddress();
			if (_address.length == 4)
			{
				return new BigInteger(1, _address).toString();
			}
			else
			{
				return _localHost.getHostAddress();
			}
		}
		catch (UnknownHostException aExc) 
		{
			LOG.error("", aExc);
			throw new ApiException(aExc);
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
