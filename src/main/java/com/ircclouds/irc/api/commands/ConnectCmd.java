package com.ircclouds.irc.api.commands;

import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.utils.*;

public class ConnectCmd implements ICommand
{
	private static final String COLUMN = ":";
	private static final String SPACE = " ";
	private static final String CRNL = "\r\n";
	private static final String NICK = "NICK";
	private static final String USER = "USER";
	private static final String PASSWORD = "PASS";

	private final String nick;
	private final String ident;
	private final String realname;
	private final String password;
	
	private final CapCmd capInitCmd;

	public ConnectCmd(final IServerParameters aServerParameters,
			final CapCmd capInitCmd)
	{
		nick = aServerParameters.getNickname();
		ident = aServerParameters.getIdent();
		realname = aServerParameters.getRealname();
		password = aServerParameters.getServer().getPassword();
		this.capInitCmd = capInitCmd;
	}

	public String asString()
	{
		return new StringBuffer()
				.append(capInitCmd == null ? "" : capInitCmd.asString())
				.append(getPassword())
				.append(NICK).append(SPACE).append(nick).append(CRNL)
				.append(USER).append(SPACE).append(ident).append(SPACE).append("0").append(SPACE).append("*").append(SPACE).append(COLUMN).append(realname).append(CRNL).toString();
	}

	private String getPassword()
	{
		if (!StringUtils.isEmpty(password))
		{
			return PASSWORD + " " + password + CRNL;
		}

		return "";
	}
}
