package com.ircclouds.irc.api.commands;


public class QuitCmd implements ICommand
{
	private static final String QUIT_ID = "QUIT";

	private String quitMsg;

	public QuitCmd()
	{
		quitMsg = "";
	}	
	
	public QuitCmd(String aQuitMsg)
	{
		quitMsg = aQuitMsg;
	}

	@Override
	public String asString()
	{
		return QUIT_ID + " :" + quitMsg;
	}
}
