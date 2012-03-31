package com.ircclouds.irc.api.om;

import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.utils.*;

public class ServerMessageBuilder implements IBuilder<ServerMessage>
{

	public ServerMessage build(String aMessage)
	{
		int _indexOfCol = StringUtils.indexOf(' ', 3, aMessage);
		String _firstPart = aMessage.substring(0, _indexOfCol);
		String _secondPart = aMessage.substring(_indexOfCol + 1);

		String[] _firstPartCmpnts = _firstPart.split(" ");

		return new ServerMessage(getNumberFrom(_firstPartCmpnts[1]), _secondPart);
	}

	private Integer getNumberFrom(String aString)
	{
		try
		{
			return Integer.parseInt(aString);
		}
		catch (NumberFormatException aExc)
		{
			return 0;
		}
	}

}
