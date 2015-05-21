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
		final String[] _cmpnts1 = aString.split("@");
		final String[] _cmpnts2 = _cmpnts1[0].split("!");

		final String nick = _cmpnts2[0].substring(1);
		final String ident = _cmpnts2.length > 1 ? _cmpnts2[1] : "";
		final String hostname = _cmpnts1.length > 1 ? _cmpnts1[1] : "";

		return new WritableIRCUser(nick, ident, hostname);
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
