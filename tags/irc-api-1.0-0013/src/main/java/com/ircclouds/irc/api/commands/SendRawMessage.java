package com.ircclouds.irc.api.commands;


public class SendRawMessage implements ICommand
{
	private String text;

	public SendRawMessage(String aText)
	{
		text = aText;
	}

	public String asString()
	{
		return text;
	}
}
