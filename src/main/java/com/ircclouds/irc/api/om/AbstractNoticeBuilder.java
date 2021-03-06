package com.ircclouds.irc.api.om;

import java.util.*;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;
import com.ircclouds.irc.api.utils.*;

public abstract class AbstractNoticeBuilder implements IBuilder<IMessage>
{
	private static final String NOTICE = "NOTICE";

	public IMessage build(String aMessage)
	{
		String _components[] = aMessage.split(" ");
		if (!_components[0].contains("@"))
		{
			if (NOTICE.equals(_components[0]))
			{
				return new ServerNotice(aMessage.substring(aMessage.indexOf(NOTICE) + NOTICE.length()), null);
			}
			
			return new ServerNotice(aMessage.substring(aMessage.indexOf(':', 1) + 1), new IRCServer(_components[0].substring(1)));
		}

		WritableIRCUser _user = ParseUtils.getUser(_components[0]);
		
		UserNotice _msg = null;
		String _text = aMessage.substring(aMessage.indexOf(" :") + 2);

		if (getChannelTypes().contains(_components[2].charAt(0)))
		{
			_msg = new ChannelNotice(_user, _text, _components[2]);
		}
		else
		{
			_msg = new UserNotice(_user, _text, _components[2]);
		}

		return _msg;
	}
	
	protected abstract Set<Character> getChannelTypes();
}
