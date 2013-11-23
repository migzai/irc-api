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
	private List<IRCChannel> channels = new ArrayList<IRCChannel>();
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

	List<IRCChannel> getChannelsMutable()
	{
		return channels;
	}

	IRCChannel getChannelByNameMutable(String aChannelName)
	{
		return getChannelByNameGeneric(aChannelName, new GetChannelCallback()
		{
			@Override
			public IRCChannel onReady(IRCChannel aChan)
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
		for (IRCChannel _c : channels)
		{
			_ircChannels.add(new UnmodifiableIRCChannel(_c));
		}
		
		return Collections.unmodifiableList(_ircChannels);
	}

	public IRCChannel getChannelByName(String aChannelName)
	{
		return getChannelByNameGeneric(prependChanType(aChannelName), new GetChannelCallback()
		{
			@Override
			public IRCChannel onReady(IRCChannel aChan)
			{
				return new UnmodifiableIRCChannel(aChan);
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
	
	private interface GetChannelCallback { IRCChannel onReady(IRCChannel aChan); };
	
	private IRCChannel getChannelByNameGeneric(String aChannelName, GetChannelCallback aCallback)
	{
		for (IRCChannel _c : channels)
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
