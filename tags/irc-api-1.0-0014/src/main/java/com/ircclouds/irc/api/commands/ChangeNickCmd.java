package com.ircclouds.irc.api.commands;

public class ChangeNickCmd implements ICommand
{
	private String newNick;
	
	public ChangeNickCmd(String aNewNick)
	{
		newNick = aNewNick;
	}
	
	@Override
	public String asString()
	{
		return "NICK :" + newNick;
	}
}
