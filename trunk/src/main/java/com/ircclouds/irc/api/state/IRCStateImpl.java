package com.ircclouds.irc.api.state;

import java.util.*;

import com.ircclouds.irc.api.domain.*;

public class IRCStateImpl implements IIRCState
{
	private String nickname;
	private String ident;
	private String realname;
	private IRCServer ircServer;
	private IRCServerOptions serverOptions;
	private List<WritableIRCChannel> channels = new ArrayList<WritableIRCChannel>();
	private List<String> altNicks;
	private boolean isConnected;
	private IRCStateImpl previousState;
	
	IRCStateImpl(String aIdent, String aRealname, List<String> aAltNicks, IRCServer aIRCServer, IRCServerOptions aServerOptions)
	{
		ident = aIdent;
		realname = aRealname;
		ircServer = aIRCServer;
		serverOptions = aServerOptions;
		altNicks = aAltNicks;
	}
	
	public IRCStateImpl(String aNickname, String aIdent, String aRealname, List<String> aAltNicks, IRCServer aIRCServer, IRCServerOptions aServerOptions)
	{
		nickname = aNickname;
		ident = aIdent;
		realname = aRealname;
		ircServer = aIRCServer;
		serverOptions = aServerOptions;
		altNicks = aAltNicks;

		previousState = new IRCStateImpl(aIdent, aRealname, aAltNicks, aIRCServer, aServerOptions);
		previousState.updateNick(nickname);
	}
	
	@Override
	public String getNickname()
	{
		return nickname;
	}

	@Override
	public List<String> getAltNicks()
	{
		return Collections.unmodifiableList(altNicks);
	}

	@Override
	public String getRealname()
	{
		return realname;
	}

	@Override
	public String getIdent()
	{
		return ident;
	}
	
	void updateNick(String aNickname)
	{
		nickname = aNickname;
	}

	List<WritableIRCChannel> getChannelsMutable()
	{
		return channels;
	}

	WritableIRCChannel getWritableChannelByName(String aChannelName)
	{
		return getChannelByNameGeneric(aChannelName, new GetChannelCallback<WritableIRCChannel>()
		{
			@Override
			public WritableIRCChannel onReady(WritableIRCChannel aChan)
			{
				return aChan;
			}
		});
	}	
	
	@Override
	public IRCServer getServer()
	{
		return ircServer;
	}
	
	public List<IRCChannel> getChannels()
	{
		List<IRCChannel> _ircChannels = new ArrayList<IRCChannel>();
		for (WritableIRCChannel _c : channels)
		{
			_ircChannels.add(new IRCChannel(_c));
		}
		
		return Collections.unmodifiableList(_ircChannels);
	}

	public IRCChannel getChannelByName(String aChannelName)
	{
		return getChannelByNameGeneric(prependChanType(aChannelName), new GetChannelCallback<IRCChannel>()
		{
			@Override
			public IRCChannel onReady(WritableIRCChannel aChan)
			{
				return new IRCChannel(aChan);
			}
		});
	}

	@Override
	public IRCServerOptions getServerOptions()
	{
		return serverOptions;
	}

	@Override
	public boolean isConnected()
	{
		return isConnected;
	}

	public void setConnected(boolean aIsConnected)
	{
		isConnected = aIsConnected;
	}
	
	private String prependChanType(String aChannelName)
	{
		for (Character _c : getServerOptions().getChanTypes())
		{
			if (_c.equals(aChannelName.charAt(0)))
			{
				return aChannelName;
			}
		}

		return getServerOptions().getChanTypes().iterator().next() + aChannelName;
	}
	
	private interface GetChannelCallback<T> { T onReady(WritableIRCChannel aChan); };

	private <T> T getChannelByNameGeneric(String aChannelName, GetChannelCallback<T> aCallback)
	{
		for (WritableIRCChannel _c : channels)
		{
			if (_c.getName().equalsIgnoreCase(aChannelName))
			{
				return aCallback.onReady(_c);
			}
		}
		
		return null;
	}

	@Override
	public IIRCState getPrevious()
	{
		return previousState;
	}
}
