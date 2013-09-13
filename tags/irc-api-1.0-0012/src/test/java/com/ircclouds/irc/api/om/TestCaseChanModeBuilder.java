package com.ircclouds.irc.api.om;

import java.util.*;

import junit.framework.*;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;

public class TestCaseChanModeBuilder extends TestCase
{
	private AbstractChanModeBuilder chanModeBuilder;
	
	public void setUp()
	{
		final IRCUserStatuses _ucs = new IRCUserStatuses(new HashSet<IRCUserStatus>() {{ add(new IRCUserStatus('o', '@', 1)); }});
		
		chanModeBuilder = new AbstractChanModeBuilder()
		{
			@Override
			protected IRCServerOptions getIRCServerOptions()
			{
				return new IRCServerOptions(new Properties())
				{
					public IRCUserStatuses getUserChanStatuses()
					{
						return _ucs;
					}
					
					public ChannelModes getChannelModes()
					{
						return new ChannelModes(
							new HashSet<Character> () {{ add('e'); add('I'); add('b'); add('q'); }}, 
							new HashSet<Character> () {{ add('k'); }}, 
							new HashSet<Character> () {{ add('f'); add('l'); add('j'); }}, 
							new HashSet<Character> () {{ add('C'); add('F'); add('L'); add('M'); add('P'); add('Q'); add('c'); add('g'); add('i'); add('m'); add('n'); add('p'); add('r'); add('s'); add('t'); add('z'); }}
						);
					}
				};
			}		
		};
	}
	
	public void testAddModesOnly()
	{
		ChannelModeMessage _msg = chanModeBuilder.build(":krad!~k@unaffiliated/krad MODE #r0b0t +kt key");
		
		List<ChannelMode> _addedModes = _msg.getAddedModes();
		List<ChannelMode> _removedModes = _msg.getRemovedModes();
		
		assertTrue("+kt key".equals(_msg.getModeStr()));
		assertTrue(_addedModes != null);
		assertTrue(_addedModes.size() == 2);
		assertTrue(_addedModes.contains(new ChannelModeD('t')));
		assertTrue(_addedModes.contains(new ChannelModeB('k', "key")));
		
		assertTrue(_removedModes != null);
		assertTrue(_removedModes.size() == 0);
	}
	
	public void testRemoveModesOnly()
	{
		ChannelModeMessage _msg = chanModeBuilder.build(":krad!~k@unaffiliated/krad MODE #r0b0t -kts *");
		
		List<ChannelMode> _addedModes = _msg.getAddedModes();
		List<ChannelMode> _removedModes = _msg.getRemovedModes();
		
		assertTrue("-kts *".equals(_msg.getModeStr()));
		assertTrue(_removedModes != null);
		assertTrue(_removedModes.size() == 3);
		assertTrue(_removedModes.contains(new ChannelModeD('t')));
		assertTrue(_removedModes.contains(new ChannelModeD('s')));
		assertTrue(_removedModes.contains(new ChannelModeB('k', "*")));
		
		assertTrue(_addedModes != null);
		assertTrue(_addedModes.size() == 0);
	}
	
	public void testAddModesWith2Params()
	{
		ChannelModeMessage _msg = chanModeBuilder.build(":krad!~k@unaffiliated/krad MODE #r0b0t +kltn key 4");
		
		List<ChannelMode> _addedModes = _msg.getAddedModes();
		List<ChannelMode> _removedModes = _msg.getRemovedModes();
		
		assertTrue("+kltn key 4".equals(_msg.getModeStr()));
		assertTrue(_addedModes != null);
		assertTrue(_addedModes.size() == 4);
		assertTrue(_addedModes.contains(new ChannelModeD('t')));
		assertTrue(_addedModes.contains(new ChannelModeD('n')));
		assertTrue(_addedModes.contains(new ChannelModeB('k', "*")));
		assertTrue(_addedModes.contains(new ChannelModeC('l', "6")));
		
		assertTrue(_removedModes != null);
		assertTrue(_removedModes.size() == 0);
	}
	
	public void testAddRemoveModes()
	{
		ChannelModeMessage _msg = chanModeBuilder.build(":krad!~k@unaffiliated/krad MODE #r0b0t +kt-ln key");
		
		List<ChannelMode> _addedModes = _msg.getAddedModes();
		List<ChannelMode> _removedModes = _msg.getRemovedModes();
		
		assertTrue("+kt-ln key".equals(_msg.getModeStr()));
		assertTrue(_addedModes != null);
		assertTrue(_addedModes.size() == 2);
		assertTrue(_addedModes.contains(new ChannelModeD('t')));

		assertTrue(_addedModes.contains(new ChannelModeB('k', "*")));
		
		assertTrue(_removedModes != null);
		assertTrue(_removedModes.size() == 2);
		assertTrue(_removedModes.contains(new ChannelModeC('l')));
		assertTrue(_removedModes.contains(new ChannelModeD('n')));
	}
	
	public void testRemoveLimitOnly()
	{
		ChannelModeMessage _msg = chanModeBuilder.build(":krad!~k@unaffiliated/krad MODE #r0b0t -l");
		
		assertTrue(_msg.getRemovedModes().contains(new ChannelModeC('l')));			
	}
	
	public void testAddRemoveAddRemove()
	{
		ChannelModeMessage _msg = chanModeBuilder.build(":krad!~k@unaffiliated/krad MODE #r0b0t +nto-o+o-o+o-o goraaab goraaab goraaab goraaab goraaab goraaab");
		
		List<ChannelMode> _addedModes = _msg.getAddedModes();
		List<ChannelMode> _removedModes = _msg.getRemovedModes();
		
		assertTrue(_addedModes != null);
		assertTrue(_addedModes.size() == 5);
		
		assertTrue(_removedModes != null);
		assertTrue(_removedModes.size() == 3);
	}
}
