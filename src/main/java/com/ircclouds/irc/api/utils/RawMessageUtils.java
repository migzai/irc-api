package com.ircclouds.irc.api.utils;

/**
 * Utils that work with raw IRC messages.
 *
 * @author Danny van Heumen
 */
public class RawMessageUtils
{

	private RawMessageUtils() {
		// don't allow instantiation
	}

	/**
	 * Test an IRC message to find out if it is a Server Numeric Message.
	 *
	 * @param aMsg the raw IRC message
	 * @return Returns true if aMsg is a server numeric message, or false
	 * otherwise.
	 */
	public static boolean isServerNumericMessage(final String aMsg)
	{
		String[] parts = aMsg.split(" ");
		if (parts.length <= 1)
		{
			return false;
		}
		try
		{
			Integer.parseInt(parts[1]);
			return true;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}
}
