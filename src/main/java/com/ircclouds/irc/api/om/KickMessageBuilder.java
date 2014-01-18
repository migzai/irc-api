package com.ircclouds.irc.api.om;

import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.utils.*;

public class KickMessageBuilder implements IBuilder<ChannelKick>
{
	@Override
	public ChannelKick build(String aMessage)
	{
		String[] _cmpnts = aMessage.split(" ");

		return new ChannelKick(ParseUtils.getUser(_cmpnts[0]), aMessage.substring(aMessage.indexOf(" :") + 2), _cmpnts[2], _cmpnts[3]);
	}
}
