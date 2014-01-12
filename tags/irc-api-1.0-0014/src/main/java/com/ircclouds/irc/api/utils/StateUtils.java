package com.ircclouds.irc.api.utils;

import java.util.*;

import com.ircclouds.irc.api.domain.*;

public class StateUtils
{
	public static WritableIRCChannel cloneChannel(IRCChannel aChan)
	{
		WritableIRCChannel _copy = new WritableIRCChannel(aChan.getName());
		_copy.setTopic(cloneTopic(aChan.getTopic()));
		cloneUsers(aChan, _copy);		
		return _copy;
	}

	public static void cloneUsers(IRCChannel aChan, WritableIRCChannel aCopy)
	{
		for (IRCUser _user : aChan.getUsers())
		{
			Set<IRCUserStatus> _cUS = new HashSet<IRCUserStatus>();
			for (IRCUserStatus _us : aChan.getStatusesForUser(_user))
			{
				_cUS.add(cloneUserStatus(_us));
			}			
			aCopy.addUser(cloneUser(_user), _cUS);			
		}
	}
	
	public static WritableIRCTopic cloneTopic(IRCTopic aTopic)
	{
		return new WritableIRCTopic(aTopic.getSetBy(), aTopic.getDate(), aTopic.getValue());
	}
	
	public static WritableIRCUser cloneUser(IRCUser aUser)
	{
		return new WritableIRCUser(aUser.getNick(), aUser.getIdent(), aUser.getHostname());
	}
	
	public static IRCUserStatus cloneUserStatus(IRCUserStatus aStatus)
	{
		return new IRCUserStatus(aStatus.getChanModeType(), aStatus.getPrefix(), aStatus.getPriority());
	}
}