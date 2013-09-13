package com.ircclouds.irc.api.commands;


public class PartChanCmd implements ICommand
{
	private static final String PART_ID = "PART";

	private String channel;
	private String partMsg;

	public PartChanCmd(String aChannel, String aPartMsg)
	{
		channel = aChannel;
		partMsg = aPartMsg;
	}

	@Override
	public String asString()
	{
		return PART_ID + " " + channel + " :" + partMsg;
	}

}
