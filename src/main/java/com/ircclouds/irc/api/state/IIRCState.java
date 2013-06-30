package com.ircclouds.irc.api.state;

import java.util.*;

import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.domain.*;

/**
 * This interface represents a view about the currently established IRC connection state, and allows retrieval 
 * of various IRC server options like channels modes, user statuses, and more, once
 * {@link IRCApiImpl#IRCApiImpl(Boolean)} is set to true.
 * 
 * @author miguel@lebane.se
 *
 */
public interface IIRCState
{
	String getNickname();

	List<String> getAltNicks();

	String getRealname();

	String getIdent();

	List<IRCChannel> getChannels();

	IRCChannel getChannelByName(String aChannelName);

	IRCServer getServer();

	IRCServerOptions getServerOptions();
	
	boolean isConnected();
	
	IIRCState getPrevious();
}
