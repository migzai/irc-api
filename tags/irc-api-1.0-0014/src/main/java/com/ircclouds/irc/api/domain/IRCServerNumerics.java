package com.ircclouds.irc.api.domain;

/**
 * 
 * @author
 * 
 */
public final class IRCServerNumerics
{

	private IRCServerNumerics()
	{

	}

	public static final int SERVER_WELCOME_MESSAGE = 1;
	public static final int SERVER_OPTIONS = 5;
	public static final int CHANNEL_TOPIC = 332;
	public static final int TOPIC_USER_DATE = 333;
	public static final int CHANNEL_NICKS_LIST = 353;
	public static final int CHANNEL_NICKS_END_OF_LIST = 366;
	public static final int END_OF_MOTD = 376;
	public static final int NO_SUCH_NICK_CHANNEL = 401;
	public static final int NO_SUCH_CHANNEL = 403;
	public static final int NO_EXTERNAL_CHANNEL_MESSAGES = 404;
	public static final int MOTD_FILE_MISSING = 422;
	public static final int ERRONEUS_NICKNAME = 432;
	public static final int NICKNAME_IN_USE = 433;
	public static final int ERR_NICKTOOFAST = 438;
	public static final int CHANNEL_FORWARD = 470;
	public static final int CHANNEL_CANNOT_JOIN_INVITE = 473;
	public static final int CHANNEL_CANNOT_JOIN_BANNED = 474;
	public static final int CHANNEL_CANNOT_JOIN_KEYED = 475;
	public static final int NOT_CHANNEL_OP = 482;
}