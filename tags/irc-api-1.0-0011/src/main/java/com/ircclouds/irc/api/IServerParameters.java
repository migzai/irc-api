package com.ircclouds.irc.api;

import java.util.*;

import com.ircclouds.irc.api.domain.*;

/**
 * This interface stores the IRC connection parameters needed on connect.
 * 
 * @author miguel@lebane.se
 *
 */
public interface IServerParameters
{
	/**
	 * Returns the desired to use nickname
	 * @return
	 */
	String getNickname();

	/**
	 * Returns desired to use alternative nicknames
	 * @return
	 */
	List<String> getAlternativeNicknames();

	/**
	 * Returns the desired to use ident
	 * @return
	 */
	String getIdent();

	/**
	 * Returns the desired to use real name
	 * @return
	 */
	String getRealname();

	/**
	 * Returns the desired to use {@link IRCServer}
	 * @return
	 */
	IRCServer getServer();
}
