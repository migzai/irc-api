package com.ircclouds.irc.api;

import java.io.*;
import java.util.*;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.state.*;

public class Test
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
	
		
		final IRCApi _api = new IRCApiImpl(false);
		_api.connect(getServerParams(), new Callback<IIRCState>()
		{
			@Override
			public void onSuccess(IIRCState aObject)
			{
				_api.actInPrivate("krad", "ssdfsfdf");
			//	_api.dccSend("Android65", new File("c:\\blabla.zip"));				
			}
			
			@Override
			public void onFailure(String aErrorMessage)
			{
				// TODO Auto-generated method stub
				
			}
		});
	}

	private static IServerParameters getServerParams()
	{
		// TODO Auto-generated method stub
		return new IServerParameters()
		{
			
			@Override
			public IRCServer getServer()
			{
				// irc.paraphysics.net
				return new IRCServer("localhost");
			}
			
			@Override
			public String getRealname()
			{
				// TODO Auto-generated method stub
				return "hello";
			}
			
			@Override
			public String getNickname()
			{
				// TODO Auto-generated method stub
				return "botety";
			}
			
			@Override
			public String getIdent()
			{
				// TODO Auto-generated method stub
				return "id";
			}
			
			@Override
			public List<String> getAlternativeNicknames()
			{
				// TODO Auto-generated method stub
				return new ArrayList<String>() { { add("aa"); } };
			}
		};
	}

}
