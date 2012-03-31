package com.ircclouds.irc.api;

import java.io.*;
import java.util.*;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.state.*;

public class TestMain
{
	static IRCApi _api = new IRCApiImpl(true);
	static Callback<IRCChannel> joinCB = new Callback<IRCChannel>()
	{
		@Override
		public void onSuccess(IRCChannel aObject)
		{
			System.out.println("joined " + aObject.getName());
			partChan(aObject.getName());
		}

		@Override
		public void onFailure(String aErrorMessage)
		{
			System.out.println(aErrorMessage);
		}
	};

	static Callback<String> partCB = new Callback<String>()
	{
		@Override
		public void onSuccess(String aObject)
		{
			System.out.println("left " + aObject);
			joinChan(aObject.substring(0, 5) + (Integer.parseInt(aObject.substring(5)) + 1));
		}

		@Override
		public void onFailure(String aErrorMessage)
		{
			throw new RuntimeException(aErrorMessage);
		}
	};

	public static void main(String[] aArgs) throws IOException
	{
		Thread.currentThread().setName("MainThread");
		_api.connect(getParams(), new Callback<IIRCState>()
		{
			@Override
			public void onSuccess(final IIRCState aObject)
			{
				try
				{
					new Thread()
					{
						public void run()
						{
							while (true)
							{
								try
								{
									Thread.sleep(2000);
									StringBuffer _sb = new StringBuffer();
									for (IRCChannel _chan : aObject.getChannels())
									{
										_sb.append(_chan.getName() + " ");
									}
									System.out.println(_sb.toString());

								}
								catch (InterruptedException e)
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}.start();
				}
				catch (Exception a)
				{
					a.printStackTrace();
				}

				joinChan("#aaaa1");
				joinChan("#bbbb1");
				joinChan("#cccc1");
				joinChan("#dddd1");
				joinChan("#eeee1");
				joinChan("#ffff1");
				// joinChan("#gggg1");
				// joinChan("#hhhh1");
				// joinChan("#iiii1");
				// joinChan("#jjjj1");
				// joinChan("#kkkk1");
				// joinChan("#llll1");
				// joinChan("#mmmm1");
				// joinChan("#nnnn1");
				// joinChan("#oooo1");
				// joinChan("#pppp1");
				// joinChan("#qqqq1");
				// joinChan("#rrrr1");
				// joinChan("#ssss1");
				// joinChan("#tttt1");
				// joinChan("#uuuu1");
				// joinChan("#vvvv1");
				// joinChan("#wwww1");
				// joinChan("#xxxx1");
				// joinChan("#yyyy1");
				// joinChan("#zzzz1");
			}

			@Override
			public void onFailure(String aErrorMessage)
			{
				throw new RuntimeException(aErrorMessage);
			}
		});
	}

	private static void joinChan(String chan)
	{
		try
		{
			_api.joinChannelAsync(chan, joinCB);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void partChan(String chan)
	{
		try
		{
			_api.leaveChannelAsync(chan, partCB);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static IServerParameters getParams()
	{
		return new IServerParameters()
		{
			@Override
			public IRCServer getServer()
			{
				return new IRCServer("localhost", true);
			}

			@Override
			public String getRealname()
			{
				return "real name";
			}

			@Override
			public String getNickname()
			{
				return "nicki";
			}

			@Override
			public String getIdent()
			{
				return "ident";
			}

			@Override
			public List<String> getAlternativeNicknames()
			{
				return Arrays.asList("one", "two");
			}
		};
	}
}
