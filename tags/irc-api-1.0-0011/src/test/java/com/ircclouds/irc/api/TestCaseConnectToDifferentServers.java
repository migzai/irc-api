package com.ircclouds.irc.api;

import java.util.*;

import org.junit.*;

import com.ircclouds.irc.api.MockUtils.ConnectedApi;
import com.ircclouds.irc.api.comms.*;
import com.ircclouds.irc.api.domain.*;

public class TestCaseConnectToDifferentServers
{
	@Test
	public void connectToEFnetMzima() throws Exception
	{
		ConnectedApi _api = MockUtils.newConnectedApi(new MockConnectionImpl("irc.mzima.net"), newMockServerParameters(), 2);

		assertNotNull(_api);
		Assert.assertEquals("HAHAA", _api.getConnectedState().getNickname());
		assertUserStatuses(_api.getConnectedState().getServerOptions().getUserChanStatuses());
		assertChannelModes(_api.getConnectedState().getServerOptions().getChannelModes(), new HashSet<Character>()
		{
			{
				add('e');
				add('I');
				add('b');
			}
		}, new HashSet<Character>()
		{
			{
				add('k');
			}
		}, new HashSet<Character>()
		{
			{
				add('l');
			}
		}, new HashSet<Character>()
		{
			{
				add('i');
				add('m');
				add('n');
				add('p');
				add('s');
				add('t');
			}
		});
	}

	@Test
	public void connectToFreenode() throws Exception
	{
		ConnectedApi _api = MockUtils.newConnectedApi(new MockConnectionImpl("irc.freenode.net"), newMockServerParameters(), 2);
		Assert.assertEquals("hahaeheh", _api.getConnectedState().getNickname());
		assertUserStatuses(_api.getConnectedState().getServerOptions().getUserChanStatuses());
		assertChannelModes(_api.getConnectedState().getServerOptions().getChannelModes(), new HashSet<Character>()
		{
			{
				add('e');
				add('I');
				add('b');
				add('q');
			}
		}, new HashSet<Character>()
		{
			{
				add('k');
			}
		}, new HashSet<Character>()
		{
			{
				add('f');
				add('l');
				add('j');
			}
		}, new HashSet<Character>()
		{
			{
				add('C');
				add('F');
				add('L');
				add('M');
				add('P');
				add('Q');
				add('c');
				add('g');
				add('i');
				add('m');
				add('n');
				add('p');
				add('s');
				add('t');
				add('r');
				add('z');
			}
		});
	}

	@Test
	public void connectToDALnetMesra() throws Exception
	{
		ConnectedApi _api = MockUtils.newConnectedApi(new MockConnectionImpl("mesra.dal.net"), newMockServerParameters(), 2);
		Assert.assertEquals("hasdd", _api.getConnectedState().getNickname());
		assertUserStatuses(_api.getConnectedState().getServerOptions().getUserChanStatuses());
		assertChannelModes(_api.getConnectedState().getServerOptions().getChannelModes(), new HashSet<Character>()
		{
			{
				add('e');
				add('I');
				add('b');
			}
		}, new HashSet<Character>()
		{
			{
				add('k');
			}
		}, new HashSet<Character>()
		{
			{
				add('j');
				add('l');
			}
		}, new HashSet<Character>()
		{
			{
				add('c');
				add('i');
				add('m');
				add('M');
				add('n');
				add('O');
				add('p');
				add('r');
				add('R');
				add('s');
				add('S');
				add('t');
			}
		});
	}

	private void assertUserStatus(IRCUserStatus aExpectedUserStatus, IRCUserStatus aActualUserStatus)
	{
		Assert.assertNotNull(aActualUserStatus);
		Assert.assertEquals(aExpectedUserStatus.getPriority(), aActualUserStatus.getPriority());
		Assert.assertEquals(aExpectedUserStatus.getPrefix(), aActualUserStatus.getPrefix());
		Assert.assertEquals(aExpectedUserStatus.getChanModeType(), aActualUserStatus.getChanModeType());
	}

	private MockServerParametersImpl newMockServerParameters()
	{
		return new MockServerParametersImpl("test", Arrays.asList("heh1", "heh2"), "ident", "a real name", new IRCServer("irc.mzima.net"));
	}

	private void assertUserStatuses(IRCUserStatuses aIRCUserStatuses)
	{
		Assert.assertTrue(aIRCUserStatuses.contains('o'));
		assertUserStatus(new IRCUserStatus('o', '@', 1), aIRCUserStatuses.getUserStatus('o'));

		Assert.assertTrue(aIRCUserStatuses.contains('v'));
		assertUserStatus(new IRCUserStatus('v', '+', 2), aIRCUserStatuses.getUserStatus('v'));
	}

	private void assertNotNull(ConnectedApi aConnectedApi)
	{
		Assert.assertNotNull(aConnectedApi.getConnectedState().getNickname());
		Assert.assertNotNull(aConnectedApi.getConnectedState());
		Assert.assertNotNull(aConnectedApi.getConnectedState().getServerOptions());
		Assert.assertNotNull(aConnectedApi.getConnectedState().getServerOptions().getChannelModes());
	}

	private void assertChannelModes(ChannelModes aChannelModes, Set<Character> aTypeA, Set<Character> aTypeB, Set<Character> aTypeC, Set<Character> aTypeD)
	{
		AssertUtils.assertSets(aChannelModes.getTypeA(), aTypeA);
		AssertUtils.assertSets(aChannelModes.getTypeB(), aTypeB);
		AssertUtils.assertSets(aChannelModes.getTypeC(), aTypeC);
		AssertUtils.assertSets(aChannelModes.getTypeD(), aTypeD);
	}
}
