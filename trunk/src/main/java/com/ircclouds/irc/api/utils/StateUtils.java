package com.ircclouds.irc.api.utils;

import java.util.*;
import java.util.Map.Entry;

import com.ircclouds.irc.api.domain.*;

public class StateUtils
{
	public static IRCChannel cloneChannel(IRCChannel aChan)
	{
		IRCChannel _copy = new IRCChannel();		
		_copy.setName(aChan.getName());
		if (aChan.getTopic() != null)
		{
			_copy.setTopic(cloneTopic(aChan.getTopic()));
		}
		_copy.setUsers(cloneUsers(aChan.getUsers()));
		
		return _copy;
	}

	public static Map<IRCUser, Set<IRCUserStatus>> cloneUsers(Map<IRCUser, Set<IRCUserStatus>> aUsers)
	{
		Map<IRCUser, Set<IRCUserStatus>> _uCopy = new HashMap<IRCUser, Set<IRCUserStatus>>();
		for (Entry<IRCUser, Set<IRCUserStatus>> _entry : aUsers.entrySet())
		{
			Set<IRCUserStatus> _cUS = new HashSet<IRCUserStatus>();
			for (IRCUserStatus _us : _entry.getValue())
			{
				_cUS.add(cloneUserStatus(_us));
			}
			_uCopy.put(cloneUser(_entry.getKey()), _cUS);
		}
		
		return _uCopy;
	}
	
	public static IRCTopic cloneTopic(IRCTopic aTopic)
	{
		return new IRCTopic(aTopic.getSetBy(), aTopic.getDate(), aTopic.getValue());
	}
	
	public static IRCUser cloneUser(IRCUser aUser)
	{
		IRCUser _copy = new IRCUser();
		_copy.setNick(aUser.getNick());
		_copy.setIdent(aUser.getIdent());
		_copy.setHostname(aUser.getHostname());
		
		return _copy;
	}
	
	public static IRCUserStatus cloneUserStatus(IRCUserStatus aStatus)
	{
		return new IRCUserStatus(aStatus.getChanModeType(), aStatus.getPrefix(), aStatus.getPriority());
	}
}