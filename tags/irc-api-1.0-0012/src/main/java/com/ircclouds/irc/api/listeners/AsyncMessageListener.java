package com.ircclouds.irc.api.listeners;

import java.util.*;

import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;

public class AsyncMessageListener
{
	private static final List<Integer> NUMERICS = Arrays.asList(IRCServerNumerics.NO_SUCH_NICK_CHANNEL, IRCServerNumerics.NO_EXTERNAL_CHANNEL_MESSAGES);
	
	private Queue<AsyncTriple> myQueue = new LinkedList<AsyncTriple>();
	
	public void onServerMsg(ServerNumericMessage aMsg)
	{
		if (NUMERICS.contains(aMsg.getNumericCode()))
		{
			String aText = aMsg.getText();
			String cmpnts[] = aText.split(" :");

			AsyncTriple _aTrip = myQueue.peek();
			if (_aTrip != null)
			{
				if (_aTrip.asyncId.equals(cmpnts[0]))
				{
					_aTrip = myQueue.poll();
					if (!_aTrip.flag)
					{
						_aTrip.callback.onSuccess("OK");
					}
				}
				else
				{
					_aTrip.callback.onFailure(new IRCException(aText));
					_aTrip.flag = true;
				}
			}
		}
	}

	public void submit(int aAsyncId, Callback<String> aCallback)
	{
		myQueue.add(new AsyncTriple(aAsyncId + "", aCallback));
	}

	private class AsyncTriple
	{
		private String asyncId;
		private Callback<String> callback;
		private boolean flag;

		public AsyncTriple(String aSyncId, Callback<String> aCb)
		{
			asyncId = aSyncId;
			callback = aCb;
		}
	}
}
