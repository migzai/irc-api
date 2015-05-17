package com.ircclouds.irc.api.om;

import com.ircclouds.irc.api.domain.IRCUser;
import com.ircclouds.irc.api.domain.messages.AwayMessage;
import com.ircclouds.irc.api.utils.ParseUtils;

/**
 * Away message builder.
 *
 * Message builder for IRCv3 away-notify capability notification messages.
 *
 * @author Danny van Heumen
 */
public class AwayMessageBuilder implements IBuilder<AwayMessage>
{

	@Override
	public AwayMessage build(String aMessage)
	{
		final String[] _cmpnts = aMessage.split(" ");
		final IRCUser user = ParseUtils.getUser(_cmpnts[0]);
		final int idx = aMessage.indexOf(" :");
		final String message;
		if (idx == -1)
		{
			message = null;
		}
		else
		{
			final String msg = aMessage.substring(idx+2);
			message = msg.isEmpty() ? null : msg;
		}
		return new AwayMessage(user, message);
	}
}
