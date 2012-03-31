package com.ircclouds.irc.api;

import java.util.*;

import com.ircclouds.irc.api.domain.*;

public interface IServerParameters
{
	String getNickname();

	List<String> getAlternativeNicknames();

	String getIdent();

	String getRealname();

	IRCServer getServer();
}
