package com.ircclouds.irc.api.om;

import com.ircclouds.irc.api.domain.messages.*;

public class PingMessageBuilder implements IBuilder<AbstractPingMessage>
{
	public AbstractPingMessage build(String aMessage)
	{
		String _cmpnt[] = aMessage.split(":");

		AbstractPingMessage _pMsg = new ServerPing();
		_pMsg.setReplyText(_cmpnt[1]);

		return _pMsg;
	}
}
