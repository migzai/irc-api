package com.ircclouds.irc.api.commands;

/**
 * CAP LS. Command to query available capabilities.
 *
 * @author Danny van Heumen
 */
public class CapLsCmd extends CapCmd
{

	@Override
	public String asString()
	{
		return "CAP LS";
	}
}
