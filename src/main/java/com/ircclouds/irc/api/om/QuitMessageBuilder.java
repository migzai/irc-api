package com.ircclouds.irc.api.om;

import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.utils.*;

public class QuitMessageBuilder implements IBuilder<QuitMessage>
{
	public QuitMessage build(String aMsg)
	{
		return new QuitMessage(ParseUtils.getUser(aMsg.split(" ")[0]), aMsg.substring(aMsg.indexOf(':', 1) + 1));
	}
}
