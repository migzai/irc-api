package com.ircclouds.irc.api.domain;

import java.util.*;

public class IRCUserStatuses implements Iterable<IRCUserStatus>
{
	private Set<IRCUserStatus> userStatuses;

	public IRCUserStatuses(Set<IRCUserStatus> aUserStatuses)
	{
		userStatuses = aUserStatuses;
	}
	
	public boolean isEmpty()
	{
		return userStatuses.isEmpty();
	}

	@Override
	public Iterator<IRCUserStatus> iterator()
	{
		return Collections.unmodifiableSet(userStatuses).iterator();
	}
	
	public boolean contains(Character aChanMode)
	{
		for (IRCUserStatus _ucs : userStatuses)
		{
			if (_ucs.getChanModeType().equals(aChanMode))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public IRCUserStatus getUserStatus(Character aChanMode)
	{
		for (IRCUserStatus _ucs : userStatuses)
		{
			if (_ucs.getChanModeType().equals(aChanMode))
			{
				return _ucs;
			}
		}
		
		return null;
	}
	
	public static IRCUserStatus getDominantUserStatus(Set<IRCUserStatus> aUserStatuses)
	{
		IRCUserStatus _ucs = null;
		int _priority = Integer.MAX_VALUE;
		
		for (IRCUserStatus _tmp : aUserStatuses)
		{
			if (_tmp.getPriority() < _priority)
			{
				_priority = _tmp.getPriority();
				_ucs = _tmp;
			}
		}
		
		return _ucs;
	}
	
	public String toString()
	{
		return userStatuses.toString();
	}
}
