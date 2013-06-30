package com.ircclouds.irc.api.listeners;

import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class SetTopicListener implements IMessageListener
{
	private Callback<String> callback;

	public SetTopicListener(Callback<String> aCallback)
	{
		callback = aCallback;
	}

	@Override
	public void onMessage(IMessage aMessage)
	{
		if (aMessage instanceof TopicMessage)
		{
			TopicMessage _topicMsg = (TopicMessage) aMessage;

			callback.onSuccess(_topicMsg.getTopic().getValue());
		}
		else if (aMessage instanceof ServerNumericMessage)
		{
			if (((ServerNumericMessage) aMessage).getNumericCode().equals(IRCServerNumerics.NOT_CHANNEL_OP))
			{
				callback.onFailure(new IRCException(((ServerNumericMessage) aMessage).getText()));
			}
		}
	}
}
