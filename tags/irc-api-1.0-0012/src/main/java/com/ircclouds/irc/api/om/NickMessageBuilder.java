package com.ircclouds.irc.api.om;

import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.utils.*;

public class NickMessageBuilder implements IBuilder<NickMessage>
{
	//:krad!k@bot.lebane.se NICK :haha

	@Override
	public NickMessage build(String aMessage)
	{
		return new NickMessage(ParseUtils.getUser(aMessage.split(" ")[0]), aMessage.substring(aMessage.indexOf(':', 1) + 1));
	}

}
