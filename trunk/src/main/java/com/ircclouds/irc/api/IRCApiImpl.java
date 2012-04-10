package com.ircclouds.irc.api;

import java.io.*;
import java.util.*;

import org.apache.log4j.*;
import org.slf4j.*;
import org.slf4j.Logger;

import com.ircclouds.irc.api.commands.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.exceptions.*;
import com.ircclouds.irc.api.listeners.*;
import com.ircclouds.irc.api.state.*;

public class IRCApiImpl implements IRCApi
{
	private static final Logger LOG = LoggerFactory.getLogger(IRCApiImpl.class);

	private IIRCSession session;
	private AbstractExecuteCommandListener executeCmdListener;
	private IIRCState state;

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
		};
	
		session.addListeners(
				executeCmdListener = new ExecuteCommandListenerImpl(session, getStateUpdater(aSaveIRCState)),
				new PingVersionListenerImpl(session));
	}

	@Override
	public void connect(final IServerParameters aServerParameters, final Callback<IIRCState> aCallback) throws IOException
	{
		if (state.isConnected())
		{
			throw new AlreadyConnectedException();
		}

		executeCmdListener.submitConnectCallback(newConnectCallback(aCallback), aServerParameters);

		boolean _isOpen = false;
		try
		{
			if (_isOpen = session.open(aServerParameters.getServer()))
			{
				session.execute(new ConnectCmd(aServerParameters));
			}
			else
			{
				aCallback.onFailure("Failed to open connection to [" + aServerParameters.getServer().toString() + "]");
			}
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
	public void disconnect(String aQuitMessage) throws IOException
	{
		checkConnected();

		session.execute(new QuitCmd(aQuitMessage));
		
		((IRCStateImpl) (state)).setConnected(false);
	}

	@Override
	public void joinChannel(String aChannelName) throws IOException
	{
		joinChannel(aChannelName, "");
	}
	
	@Override
	public void joinChannelAsync(String aChannelName, Callback<IRCChannel> aCallback) throws IOException
	{
		joinChannelAsync(aChannelName, "", aCallback);
	}

	@Override
	public void joinChannel(String aChannelName, String aKey) throws IOException
	{
		checkConnected();

		session.execute(new JoinChanCmd(prependChanType(aChannelName), aKey));
	}
	
	@Override
	public void joinChannelAsync(String aChannelName, String aKey, final Callback<IRCChannel> aCallback) throws IOException
	{
		checkConnected();

		aChannelName = prependChanType(aChannelName);

		executeCmdListener.submitJoinChannelCallback(aChannelName, aCallback);
		session.execute(new JoinChanCmd(aChannelName, aKey));
	}

	@Override
	public void leaveChannel(String aChannelName) throws IOException
	{
		leaveChannel(aChannelName, "");
	}
	
	@Override
	public void leaveChannelAsync(String aChannelName, Callback<String> aCallback) throws IOException
	{
		leaveChannelAsync(aChannelName, "", aCallback);
	}
	
	@Override
	public void leaveChannel(String aChannelName, String aPartMessage) throws IOException
	{
		checkConnected();

		session.execute(new PartChanCmd(aChannelName, aPartMessage));
	}
	
	@Override
	public void leaveChannelAsync(String aChannelName, String aPartMessage, Callback<String> aCallback) throws IOException
	{
		checkConnected();

		executeCmdListener.submitPartChannelCallback(aChannelName, aCallback);
		session.execute(new PartChanCmd(aChannelName, aPartMessage));
	}

	@Override
	public void sendChannelMessage(String aChannelName, String aMessage) throws IOException
	{
		checkConnected();

		session.execute(new SendChannelMessage(aChannelName, aMessage));
	}

	@Override
	public void sendPrivateMessage(String aNick, String aText) throws IOException
	{
		checkConnected();

		session.execute(new SendPrivateMessage(aNick, aText));
	}

	@Override
	public void actInChannel(String aChannelName, String aActionMessage) throws IOException
	{
		checkConnected();

		session.execute(new SendChannelActionMessage(aChannelName, aActionMessage));
	}

	@Override
	public void actInPrivate(String aChannelName, String aActionMessage) throws IOException
	{
		checkConnected();

		session.execute(new SendPrivateActionMessage(aChannelName, aActionMessage));
	}

	@Override
	public void changeMode(String aModeString) throws IOException
	{
		checkConnected();

		session.execute(new ChangeModeCmd(aModeString));
	}

	@Override
	public void changeNick(String aNewNick) throws IOException
	{
		checkConnected();

		session.execute(new ChangeNickCmd(aNewNick));
	}

	@Override
	public void changeNickAsync(String aNewNickname, Callback<String> aCallback) throws IOException
	{
		checkConnected();
		
		executeCmdListener.submitChangeNickCallback(aNewNickname, aCallback);
		session.execute(new ChangeNickCmd(aNewNickname));
	}	
	
	@Override
	public void changeTopic(final String aChannel, final String aSuggestedTopic) throws IOException
	{
		checkConnected();

		session.execute(new ChangeTopicCmd(aChannel, aSuggestedTopic));
	}

	@Override
	public void sendRawMessage(String aMessage) throws IOException
	{
		session.execute(new SendRawMessage(aMessage));
	}
	
	@Override
	public void addListener(IMessageListener aListener)
	{
		session.addListeners(aListener);
	}

	@Override
	public void deleteListener(IMessageListener aListener)
	{
		session.removeListener(aListener);
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
			throw new NotConnectedException();
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
			session.addListeners((AbstractIRCStateUpdater) _stateUpdater);
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
