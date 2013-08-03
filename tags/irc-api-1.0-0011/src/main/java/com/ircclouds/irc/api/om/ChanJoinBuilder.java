package com.ircclouds.irc.api.om;

import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.utils.*;

public class ChanJoinBuilder implements IBuilder<ChanJoinMessage>
{
	public ChanJoinMessage build(String aMessage)
	{
		String[] _cmpnts = aMessage.split(" ");

		String _chanName = _cmpnts[2];
		if (_chanName.startsWith(":"))
		{
			_chanName = _chanName.substring(1);
		}
		
		return new ChanJoinMessage(ParseUtils.getUser(_cmpnts[0]), _chanName);
	}
}
