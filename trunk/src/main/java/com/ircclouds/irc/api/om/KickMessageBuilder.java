package com.ircclouds.irc.api.om;

import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.utils.*;

public class KickMessageBuilder implements IBuilder<ChannelKickMsg>
{
	@Override
	public ChannelKickMsg build(String aMessage)
	{
		String[] _cmpnts = aMessage.split(" ");

		return new ChannelKickMsg(ParseUtils.getUser(aMessage), aMessage.substring(aMessage.indexOf(':', 1) + 1), _cmpnts[2], _cmpnts[3]);
	}
}
