package com.ircclouds.irc.api.om;

import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.utils.*;

public class NickMessageBuilder implements IBuilder<NickMessage>
{
	@Override
	public NickMessage build(String aMessage)
	{
		return new NickMessage(ParseUtils.getUser(aMessage.split(" ")[0]), aMessage.substring(aMessage.indexOf("NICK :")+6));
	}
}
