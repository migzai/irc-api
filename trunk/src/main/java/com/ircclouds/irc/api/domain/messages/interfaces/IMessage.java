package com.ircclouds.irc.api.domain.messages.interfaces;

import java.io.*;

public interface IMessage extends Serializable
{
	IMessage NO_MESSAGE = new IMessage()
	{

	};
}
