package com.ircclouds.irc.api.utils;

import java.util.*;

import com.ircclouds.irc.api.domain.*;

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
	
	public static String join(List<ChannelMode> aList)
	{
		StringBuffer _sb = new StringBuffer();
		for (ChannelMode _s : aList)
		{
			_sb.append(_s.getChannelModeType());
		}
		
		return _sb.toString();
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