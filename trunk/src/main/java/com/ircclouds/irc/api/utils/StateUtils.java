package com.ircclouds.irc.api.utils;

import java.util.*;
import java.util.Map.Entry;

import com.ircclouds.irc.api.domain.*;

public class StateUtils
{
	public static WritableIRCChannel cloneChannel(WritableIRCChannel aChan)
	{
		WritableIRCChannel _copy = new WritableIRCChannel();		
		_copy.setName(aChan.getName());
		_copy.setTopic(cloneTopic(aChan.getTopic()));
		//_copy.setUsersStatuses(cloneUsers(aChan.getUsersStatuses()));
		
		return _copy;
	}

	public static Map<WritableIRCUser, Set<IRCUserStatus>> cloneUsers(Map<WritableIRCUser, Set<IRCUserStatus>> aUsers)
	{
		Map<WritableIRCUser, Set<IRCUserStatus>> _uCopy = new HashMap<WritableIRCUser, Set<IRCUserStatus>>();
		for (Entry<WritableIRCUser, Set<IRCUserStatus>> _entry : aUsers.entrySet())
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
	
	public static WritableIRCTopic cloneTopic(WritableIRCTopic aTopic)
	{
		return new WritableIRCTopic(aTopic.getSetBy(), aTopic.getDate(), aTopic.getValue());
	}
	
	public static WritableIRCUser cloneUser(WritableIRCUser aUser)
	{
		WritableIRCUser _copy = new WritableIRCUser();
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