package com.ircclouds.irc.api.om;

import java.util.*;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.utils.*;

public abstract class AbstractPrivateMessageBuilder implements IBuilder<AbstractPrivMsg>
{
	private static final char NUL = '\001';
	private static final String EMPTY = " ";
	private static final String PING = "PING";
	private static final String VERSION = "VERSION";
	private static final String ACTION = "ACTION";

	@Override
	public AbstractPrivMsg build(String aMessage)
	{
		String _components[] = aMessage.split(EMPTY);
		WritableIRCUser _user = ParseUtils.getUser(_components[0]);

		final AbstractPrivMsg _msg;
		String _m = aMessage.substring(aMessage.indexOf(" :") + 2);

		if (!_components[2].isEmpty() && getChannelTypes().contains(_components[2].charAt(0)))
		{
			// channel msg
			final ChannelPrivMsg _cPrivMsg;
			String _chanName = _components[2];

			if (_m.length() >= 2 && _m.charAt(0) == NUL && _m.charAt(_m.length() - 1) == NUL)
			{
				String _type = _m.substring(1, _m.length() - 1);
				_m = _type;
				if (VERSION.equals(_type))
				{
					_cPrivMsg = new ChannelVersionMsg(_user, _m, _chanName);
				}
				else if (PING.equals(_type))
				{
					_cPrivMsg = new ChannelPingMsg(_user, _m, _chanName);
				}
				else if (_type.startsWith(ACTION))
				{
					_m = _m.substring(_m.indexOf(' ') + 1);
					_cPrivMsg = new ChannelActionMsg(_user, _m, _chanName);
				}
				else
				{
					_cPrivMsg = new ChannelCTCPMsg(_user, _m, _chanName);
				}
			}
			else
			{
				_cPrivMsg = new ChannelPrivMsg(_user, _m, _chanName);
			}

			_msg = _cPrivMsg;
		}
		else
		{
			// user msg
			if (_m.length() >= 2 && _m.charAt(0) == NUL && _m.charAt(_m.length() - 1) == NUL)
			{
				String _type = _m.substring(1, _m.length() - 1);
				_m = _type;
				if (VERSION.compareTo(_type) <= 0)
				{
					_msg = new UserVersion(_user, _components[2], _m);
				}
				else if (PING.compareTo(_type) <= 0)
				{
					_msg = new UserPing(_user, _components[2], _m);
				}
				else if (_type.startsWith(ACTION))
				{
					_m = _m.substring(_m.indexOf(' ') + 1);
					_msg = new UserActionMsg(_user, _components[2], _m);
				}
				else
				{
					_msg = new UserCTCPMsg(_user, _components[2], _m);
				}
			}
			else
			{
				_msg = new UserPrivMsg(_user, _components[2], _m);
			}
		}

		return _msg;
	}
	
	protected abstract Set<Character> getChannelTypes();
}
