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
	private ParseUtils()
	{

	}
	
	public static String getTextWithoutPrefix(String aText, String aPrefix)
	{
		if (aText.startsWith(aPrefix)) {
			return aText.substring(aPrefix.length());
		}
		
		return aText;
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
