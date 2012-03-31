package com.ircclouds.irc.api;

import java.util.*;

import com.ircclouds.irc.api.domain.*;

class MockServerParametersImpl implements IServerParameters
{
	private String nickname;
	private List<String> altNicks;
	private String ident;
	private String realname;
	private IRCServer server;
	
	public MockServerParametersImpl(String aNickname, List<String> aAltNicks, String aIdent, String aRealname, IRCServer aServer)
	{
		nickname = aNickname;
		altNicks = aAltNicks;
		ident = aIdent;
		realname = aRealname;
		server = aServer;
	}
	
	@Override
	public String getNickname()
	{
		return nickname;
	}

	@Override
	public List<String> getAlternativeNicknames()
	{
		return altNicks;
	}

	@Override
	public String getIdent()
	{
		return ident;
	}

	@Override
	public String getRealname()
	{
		return realname;
	}

	@Override
	public IRCServer getServer()
	{
		return server;
	}

}
