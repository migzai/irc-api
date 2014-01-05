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

	public static WritableIRCUser getUser(String aString)
	{
		String[] _cmpnts1 = aString.split("@");
		String[] _cmpnts2 = _cmpnts1[0].split("!");

		WritableIRCUser _user = new WritableIRCUser();
		_user.setHostname(_cmpnts1[1]);
		_user.setNick(_cmpnts2[0].substring(1));
		_user.setIdent(_cmpnts2[1]);

		return _user;
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
