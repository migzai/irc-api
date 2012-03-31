package com.ircclouds.irc.api;

import java.io.*;

import com.ircclouds.irc.api.commands.*;

public interface ICommandServer
{
	void execute(ICommand aCommand) throws IOException;
}
