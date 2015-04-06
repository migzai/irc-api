package com.ircclouds.irc.api.domain;

import java.util.*;

public class IRCServerOptions
{
	/*
	 * Immutable class.
	 */
	
	private static final String CHAN_TYPES_STR = "CHANTYPES";
	private static final String PREFIX_STR = "PREFIX";
	private static final String CHAN_MODES_STR = "CHANMODES";
	
	private final Set<Character> CHAN_TYPES = new LinkedHashSet<Character>();
	
	private Set<Character> TYPE_A = new HashSet<Character>();
	private Set<Character> TYPE_B = new HashSet<Character>();
	private Set<Character> TYPE_C = new HashSet<Character>();
	private Set<Character> TYPE_D = new HashSet<Character>();
	private ChannelModes CHAN_MODES = new ChannelModes(TYPE_A, TYPE_B, TYPE_C, TYPE_D);
	
	private Set<IRCUserStatus> USER_STATUSES = new HashSet<IRCUserStatus>();
	private IRCUserStatuses USER_CHAN_STATUSES = new IRCUserStatuses(USER_STATUSES);
		
	private Properties properties;
	
	public IRCServerOptions(Properties aProperties)
	{	
		properties = aProperties;
		
		loadChannelTypes();
		loadUserStatuses();
		loadChannelModes();
	}
	
	public String getKey(String aKey)
	{
		return properties.getProperty(aKey);
	}

	public Set<Character> getChanTypes()
	{
		return Collections.unmodifiableSet(CHAN_TYPES);
	}

	public IRCUserStatuses getUserChanStatuses()
	{
		return USER_CHAN_STATUSES;
	}

	public ChannelModes getChannelModes()
	{
		return CHAN_MODES;
	}

	public String toString()
	{
		return properties.toString();
	}	

	private void loadChannelModes()
	{
		String _chanModes = (String) properties.get(CHAN_MODES_STR);
		if (_chanModes != null)
		{
			String _classes[] = _chanModes.split(",");
			TYPE_A.addAll(getModes(_classes[0]));
			TYPE_B.addAll(getModes(_classes[1]));
			TYPE_C.addAll(getModes(_classes[2]));
			TYPE_D.addAll(getModes(_classes[3]));
		}
	}
	
	private void loadUserStatuses()
	{
		String _prefixes = (String) properties.get(PREFIX_STR);
		if (_prefixes != null)
		{
			int _1stBrkt = _prefixes.indexOf('(');
			int _2ndBrkt = _prefixes.indexOf(')');
			int _sub = _2ndBrkt - _1stBrkt;

			for (int _i = _1stBrkt + 1; _i < _2ndBrkt; _i++)
			{
				USER_STATUSES.add(new IRCUserStatus(_prefixes.charAt(_i), _prefixes.charAt(_i + _sub), _i));
			}
		}
	}
	
	private void loadChannelTypes()
	{
		String _cTypes = (String) properties.get(CHAN_TYPES_STR);
		if (_cTypes != null)
		{
			for (int _i = 0; _i < _cTypes.length(); _i++)
			{
				CHAN_TYPES.add(new Character(_cTypes.charAt(_i)));
			}
		}
		else
		{
			// RFC1459 clearly states the (default) possible channel types. In
			// case no channel types are defined by 005 RPL_ISUPPORT, then
			// assume these defaults.
			CHAN_TYPES.add('#');
			CHAN_TYPES.add('&');
		}
	}
	
	private Set<Character> getModes(String aModes)
	{
		Set<Character> _chars = new HashSet<Character>();
		for (int _i = 0; _i < aModes.length(); _i++)
		{
			_chars.add(aModes.charAt(_i));
		}
		return _chars;
	}
}