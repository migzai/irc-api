package com.ircclouds.irc.api.listeners;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.commands.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.state.*;

public class ConnectCmdListener
{
	private String nick;
	private IServerParameters params;

	private Properties properties = new Properties();

	private IIRCSession session;
	private Queue<String> altNicks;

	private Callback<IIRCState> callback;

	public ConnectCmdListener(IIRCSession aSession)
	{
		session = aSession;
	}

	public void setCallback(Callback<IIRCState> aCallback, IServerParameters aServerParameters)
	{
		callback = aCallback;
		params = aServerParameters;
		altNicks = new ArrayBlockingQueue<String>(aServerParameters.getAlternativeNicknames().size(), true, aServerParameters.getAlternativeNicknames());
		properties = new Properties();
	}

	public void onServerMessage(ServerMessage aServMsg)
	{
		if (aServMsg.getNumericCode() == IRCServerNumerics.NICKNAME_IN_USE)
		{
			String _altNick = null;
			if (!altNicks.isEmpty())
			{
				_altNick = altNicks.poll();
			}
			else
			{
				throw new RuntimeException("Found no more altnicks!");
			}

			try
			{
				session.getCommandServer().execute(new SendRawMessage("NICK " + _altNick + "\r\n"));
			}
			catch (IOException aExc)
			{
				throw new RuntimeException(aExc);
			}
		}
		else if (aServMsg.getNumericCode() == IRCServerNumerics.ERRONEUS_NICKNAME)
		{
			throw new RuntimeException("Errorneus nickname");
		}
		else if (aServMsg.getNumericCode() == IRCServerNumerics.SERVER_WELCOME_MESSAGE)
		{
			nick = getNick(aServMsg.getText());
		}
		else if (aServMsg.getNumericCode().equals(IRCServerNumerics.SERVER_OPTIONS))
		{
			String _opts[] = aServMsg.getText().split(" ");
			for (String _opt : _opts)
			{
				if (_opt.contains("="))
				{
					String _kv[] = _opt.split("=");
					properties.put(_kv[0], _kv[1]);
				}
			}
		}
		else if (aServMsg.getNumericCode().equals(IRCServerNumerics.MOTD_FILE_MISSING) || aServMsg.getNumericCode().equals(IRCServerNumerics.END_OF_MOTD))
		{
			callback.onSuccess(new IRCStateImpl(nick, params.getIdent(), params.getRealname(), params.getAlternativeNicknames(), params.getServer(), new IRCServerOptions(
					properties)));
		}
	}

	public void onError(ErrorMessage aMsg)
	{
		callback.onFailure(aMsg.getText());
	}

	private String getNick(String aText)
	{
		String _cmpnt[] = aText.split(" ");
		String _nick = _cmpnt[_cmpnt.length - 1];

		if (_nick.contains("!"))
		{
			_nick = _nick.substring(0, _nick.indexOf("!"));
		}

		return _nick;
	}
}
