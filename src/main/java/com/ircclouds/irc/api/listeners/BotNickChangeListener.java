package com.ircclouds.irc.api.listeners;

import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class BotNickChangeListener implements IMessageListener
{
	private Callback<String> callBack;

	@Override
	public void onMessage(IMessage aMessage)
	{
		if (aMessage instanceof NickMessage)
		{
			callBack.onSuccess(((NickMessage) aMessage).getNewNick());
		}
		else if (aMessage instanceof ServerMessage)
		{
			ServerMessage _servMsg = (ServerMessage) aMessage;
			if (_servMsg.getNumericCode().equals(IRCServerNumerics.NICKNAME_IN_USE))
			{
				callBack.onFailure(_servMsg.getText());
			}
			else if (_servMsg.getNumericCode().equals(IRCServerNumerics.ERRONEUS_NICKNAME))
			{
				callBack.onFailure(_servMsg.getText());
			}
			else if (_servMsg.getNumericCode().equals(IRCServerNumerics.ERR_NICKTOOFAST))
			{
				callBack.onFailure(_servMsg.getText());
			}
		}
	}
}
