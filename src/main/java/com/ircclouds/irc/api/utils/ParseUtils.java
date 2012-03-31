package com.ircclouds.irc.api.utils;

import com.ircclouds.irc.api.domain.*;

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

	public static IRCUser getUser(String aString)
	{
		String[] _cmpnts1 = aString.split("@");
		String[] _cmpnts2 = _cmpnts1[0].split("!");

		IRCUser _user = new IRCUser();
		_user.setHostname(_cmpnts1[1]);
		_user.setNick(_cmpnts2[0].substring(1));
		_user.setIdent(_cmpnts2[1]);

		return _user;
	}
}
