package com.ircclouds.irc.api.utils;

import java.util.*;

import com.ircclouds.irc.api.domain.*;

public class StateUtils
{
	public static WritableIRCChannel cloneChannel(WritableIRCChannel aChan)
	{
		WritableIRCChannel _copy = new WritableIRCChannel(aChan.getName());
		_copy.setTopic(cloneTopic(aChan.getTopic()));
		cloneUsers(aChan, _copy);		
		return _copy;
	}

	public static void cloneUsers(WritableIRCChannel aChan, WritableIRCChannel aCopy)
	{
		for (WritableIRCUser _user : aChan.getUsers())
		{
			Set<IRCUserStatus> _cUS = new HashSet<IRCUserStatus>();
			for (IRCUserStatus _us : aChan.getStatusesForUser(_user))
			{
				_cUS.add(cloneUserStatus(_us));
			}			
			aCopy.addUser(cloneUser(_user), _cUS);			
		}
	}
	
	public static WritableIRCTopic cloneTopic(WritableIRCTopic aTopic)
	{
		return new WritableIRCTopic(aTopic.getSetBy(), aTopic.getDate(), aTopic.getValue());
	}
	
	public static WritableIRCUser cloneUser(WritableIRCUser aUser)
	{
		return new WritableIRCUser(aUser.getNick(), aUser.getIdent(), aUser.getHostname());
	}
	
	public static IRCUserStatus cloneUserStatus(IRCUserStatus aStatus)
	{
		return new IRCUserStatus(aStatus.getChanModeType(), aStatus.getPrefix(), aStatus.getPriority());
	}
}