package com.ircclouds.irc.api.utils;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

/**
 * 
 * @author
 * 
 */
public final class ParseUtils
{
	public static void main(String[] asd)
	{
		System.out.println(getNick(":what53!m@2001:0:53aa:64c:38b6:13a1:bd22:ff77 NICK :sdfsdf"));
	}

	private ParseUtils()
	{

	}
	
	public static String getNick(String aMessage)
	{
		return aMessage.substring(aMessage.indexOf("NICK :")+6);
	}
	
	public static WritableIRCUser getUser(String aString)
	{
		String[] _cmpnts1 = aString.split("@");
		String[] _cmpnts2 = _cmpnts1[0].split("!");

		return new WritableIRCUser(_cmpnts2[0].substring(1), _cmpnts2[1], _cmpnts1[1]);
	}
	
	public static ISource getSource(String aString)
	{
		if (aString.contains("@"))
		{
			return getUser(aString);
		}
		
		return new IRCServer(aString.substring(1));
	}
}
