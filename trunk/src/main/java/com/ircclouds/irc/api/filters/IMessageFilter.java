package com.ircclouds.irc.api.filters;

import com.ircclouds.irc.api.domain.messages.interfaces.*;

public interface IMessageFilter
{
	FilterResult filter(IMessage aMessage);
}
