package com.ircclouds.irc.api.domain.messages;

import com.ircclouds.irc.api.domain.IRCUser;
import com.ircclouds.irc.api.domain.messages.interfaces.IUserMessage;

/**
 * Away notification message. (IRCv3 "away-notify" capability)
 *
 * The away message is an update for a user's presence status (away, available).
 * In case the user is available, the message will be null and {@code #isAway()}
 * will be false. If the user is away, {@code #isAway()} will be true and a away
 * message is available.
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

	/**
	 * Get away message.
	 *
	 * If away message is null this implies that user is available, if message
	 * is non-null this implies that user is away.
	 *
	 * @return Returns the away message of the user.
	 */
	public String getMessage()
	{
		return this.message;
	}

	/**
	 * Indicates whether an away message is present and the user is away.
	 *
	 * If user is away, this implies that a message is present. If user is
	 * available, this implies that message is null.
	 *
	 * @return False if user is available, true if user is away.
	 */
	public boolean isAway()
	{
		return this.message != null;
	}
}
