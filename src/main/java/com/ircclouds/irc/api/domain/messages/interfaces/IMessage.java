package com.ircclouds.irc.api.domain.messages.interfaces;

import java.io.*;

public interface IMessage extends Serializable
{	
	ISource getSource();
	
	String asRaw();
}
