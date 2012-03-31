package com.ircclouds.irc.api.om;

import java.util.*;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.utils.*;

public abstract class AbstractNoticeBuilder implements IBuilder<AbstractNotice>
{
	public AbstractNotice build(String aMessage)
	{
		String _components[] = aMessage.split(" ");

		if (!_components[0].contains("@"))
		{
			return new ServerNotice(aMessage.substring(aMessage.indexOf(':', 1) + 1));
		}

		IRCUser _user = ParseUtils.getUser(_components[0]);
		
		UserNotice _msg = null;
		
		String _newMsg = aMessage.substring(1);
		String _text = _newMsg.substring(_newMsg.indexOf(':') + 1);

		if (getChannelTypes().contains(_components[2].charAt(0)))
		{
			_msg = new ChannelNotice(_user, _text, _components[2]);
		}
		else
		{
			_msg = new UserNotice(_user, _text);
		}

		return _msg;
	}
	
	protected abstract Set<Character> getChannelTypes();
}
