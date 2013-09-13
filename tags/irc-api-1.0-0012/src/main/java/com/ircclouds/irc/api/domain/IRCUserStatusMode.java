package com.ircclouds.irc.api.domain;


public class IRCUserStatusMode extends ChannelMode
{
	private String user;
	private IRCUserStatus userStatus;
	
	public IRCUserStatusMode(IRCUserStatus aUserStatus, String aUser)
	{
		super(aUserStatus.getChanModeType());
		
		userStatus = aUserStatus;
		user = aUser;
	}
	
	public String getUser()
	{
		return user;
	}
	
	public IRCUserStatus getUserStatus()
	{
		return userStatus;
	}
	
	public boolean equals(Object aChanMode)
	{
		boolean _compr = super.equals(aChanMode);
		if (_compr && aChanMode instanceof IRCUserStatusMode)
		{
			IRCUserStatusMode _ucsm = (IRCUserStatusMode) aChanMode;
			return user.equals(_ucsm.getUser());
		}
		
		return false;
	}
	
	public int hashCode()
	{
		return super.hashCode() + user.hashCode();
	}
}
