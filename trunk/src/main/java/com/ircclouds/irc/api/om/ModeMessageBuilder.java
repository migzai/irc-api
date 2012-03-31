package com.ircclouds.irc.api.om;

import com.ircclouds.irc.api.domain.messages.*;

public class ModeMessageBuilder implements IBuilder<ChannelModeMessage>
{
	//:miguel!k@bot.lebane.se MODE #heh +ol kolera 10
	@Override
	public ChannelModeMessage build(String aMessage)
	{
		String[] _msg = aMessage.split(" ");
		if (_msg[2].startsWith("#"))
		{
			
		}
		
		// TODO Auto-generated method stub
		return null;
	}

}
