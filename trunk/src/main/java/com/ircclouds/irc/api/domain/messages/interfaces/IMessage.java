package com.ircclouds.irc.api.domain.messages.interfaces;

import java.io.*;

public interface IMessage extends Serializable
{
	IMessage NULL_MESSAGE = new IMessage()
	{
		@Override
		public ISource getSource()
		{
			return ISource.NULL_SOURCE;
		}

		@Override
		public String asRaw()
		{
			return "";
		}		
	};
	
	ISource getSource();
	
	String asRaw();
}
