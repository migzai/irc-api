package com.ircclouds.irc.api.domain.messages;

/**
 * 
 * @author
 * 
 */
public class UserPong
{
	private String nick;
	private String text;

	public UserPong(String aNick, String aText)
	{
		nick = aNick;
		text = aText;
	}

	public String toString()
	{
		return "NOTICE " + nick + " :" + text;
	}
}
