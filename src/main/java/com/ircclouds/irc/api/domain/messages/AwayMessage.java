package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.IRCUser;
import com.ircclouds.irc.api.domain.messages.interfaces.IUserMessage;

/**
 * Away notification message. (IRCv3 "away-notify" capability)
 *
 * @author Danny van Heumen
 */
public class AwayMessage implements IUserMessage
{
	private final IRCUser user;
	private final String message;

	public AwayMessage(final IRCUser aUser, final String aMessage)
	{
		this.user = aUser;
		this.message = aMessage;
	}

	@Override
	public IRCUser getSource()
	{
		return this.user;
	}

	@Override
	public String asRaw()
	{
		final StringBuilder raw = new StringBuilder(":");
		raw.append(user).append(" AWAY");
		if (this.message != null)
		{
			raw.append(" :").append(this.message);
		}
		return raw.toString();
	}

	public String getMessage()
	{
		return this.message;
	}

	public boolean isAway()
	{
		return this.message != null;
	}
}
