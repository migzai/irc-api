package com.ircclouds.irc.api;

import java.io.*;
import java.util.*;

import com.ircclouds.irc.api.ctcp.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.listeners.*;
import com.ircclouds.irc.api.state.*;

public class TestSend
{
	public static void main(String[] args)
	{
		final IRCApi _api = new IRCApiImpl(false);
		_api.addListener(new VariousMessageListenerAdapter()
		{
			@Override
			public void onUserPrivMessage(UserPrivMsg aMsg)
			{
				if (aMsg.getText().contains("DCC RESUME"))
				{
					String[] _msgs = aMsg.getText().split(" ");

					File _f = new File(_msgs[2]);

					_api.dccAccept(aMsg.getFromUser().getNick(), _f, Integer.parseInt(_msgs[3]), Integer.parseInt(_msgs[4]), new DCCSendCallback()
					{
						
						@Override
						public void onSuccess(DCCSendResult aObject)
						{
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onFailure(DCCSendResult aErrorMessage)
						{
							// TODO Auto-generated method stub
							
						}
					});
				}
			}
		});
		_api.connect(getPars(), new Callback<IIRCState>()
		{

			@Override
			public void onSuccess(IIRCState aObject)
			{
				_api.joinChannel("lol");
				_api.dccSend("krad", 1029, new File("lol.zip"), new DCCSendProgressCallback()
				{
					@Override
					public void onSuccess(DCCSendResult aObject)
					{
						System.out.println(aObject);
					}
					
					@Override
					public void onFailure(DCCSendResult aErrorMessage)
					{
						System.out.println(aErrorMessage.toString());
					}
					
					@Override
					public void onProgress(int aBytesTransferred)
					{
						System.out.println(aBytesTransferred);
					}
				});
			}

			@Override
			public void onFailure(String aErrorMessage)
			{

			}
		});
	}

	private static IServerParameters getPars()
	{
		return new IServerParameters()
		{

			@Override
			public IRCServer getServer()
			{
				return new IRCServer("192.168.1.108");
			}

			@Override
			public String getRealname()
			{
				return "aaa";
			}

			@Override
			public String getNickname()
			{
				return "botsend";
			}

			@Override
			public String getIdent()
			{
				return "b";
			}

			@Override
			public List<String> getAlternativeNicknames()
			{
				return Arrays.asList("hehe", "hahaha");
			}
		};
	}
}