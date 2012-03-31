package com.ircclouds.irc.api.utils;

/**
 * 
 * @author
 * 
 */
public final class StringUtils
{

	private StringUtils()
	{

	}

	public static int indexOf(char aChar, int aCount, String aString)
	{
		int _numSpc = 0;
		int _strLen = aString.length();
		for (int _i = 0; _i < _strLen; _i++)
		{
			if (aString.charAt(_i) == aChar)
			{
				_numSpc++;
				if (_numSpc == aCount)
				{
					return _i;
				}
			}
		}

		return -1;
	}

	public static boolean isEmpty(String aString)
	{
		return aString == null || "".equals(aString);
	}
}