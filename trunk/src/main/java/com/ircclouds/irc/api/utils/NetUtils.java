package com.ircclouds.irc.api.utils;

import java.io.*;
import java.net.*;
import java.util.*;

public class NetUtils
{
	public static Long ipAsNumeric(String ipAsString)
	{
		String[] segments = ipAsString.split("\\.");
		return (long) (Long.parseLong(segments[0]) * 16777216L + Long.parseLong(segments[1]) * 65536L + Long.parseLong(segments[2]) * 256L + Long.parseLong(segments[3]));
	}
	
	public static int getRandDccPort()
	{
		return getRandNumberIn(1024, 5000);
	}
	
	public static int getRandNumberIn(int aMin, int aMax)
	{
		if (aMin <= aMax)
		{
			throw new RuntimeException("Please provide a valid [Min-Max] range.");
		}
		
		Random _rand = new Random();
		int _port = _rand.nextInt(aMax - aMin) + aMin;
		while (!NetUtils.available(_port))
		{
			_port = _rand.nextInt(aMax - aMin) + aMin;
		}
		
		return _port;
	}
	
	public static boolean available(int aPort)
	{
		ServerSocket _ss = null;
		try
		{
			_ss = new ServerSocket(aPort);
			_ss.setReuseAddress(true);
			
			return true;
		}
		catch (IOException aExc)
		{
		}
		finally
		{
			if (_ss != null)
			{
				try
				{
					_ss.close();
				}
				catch (IOException aExc)
				{
				}
			}
		}

		return false;
	}
}
