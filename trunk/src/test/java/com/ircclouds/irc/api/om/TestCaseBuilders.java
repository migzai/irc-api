package com.ircclouds.irc.api.om;

import java.util.*;

import junit.framework.*;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.utils.*;

/**
 * Oh yes first tests :O
 * @author yoann
 *
 */
public class TestCaseBuilders extends TestCase
{

	private static final String TEST_CHANNEL = "#math";
	private static final IRCUser   TEST_USER;
	
	static 
	{
	    TEST_USER = new IRCUser();
	    TEST_USER.setHostname("dyoann.dyndns.org");
	    TEST_USER.setIdent("k");
	    TEST_USER.setNick("soka");
	}
	
	private static final String USER_STRING  = ":"+TEST_USER.getNick()+"!"+TEST_USER.getIdent()+"@"+TEST_USER.getHostname();
	
	/**
	 * Test against dummy values
	 * @param aUser
	 * @param aChannel
	 */
	private void checkChannelAndUser(IRCUser aUser, String aChannel, boolean removeDash)
	{
		if(removeDash)
		{
			assertEquals(TEST_CHANNEL,aChannel);	
		}
		else
		{
			assertEquals(TEST_CHANNEL,aChannel);
		}
		
		assertEquals(TEST_USER, aUser);
	}
	
	/**
	 * Test to build a JOIN MESSAGE
	 */
	public void testChanJoinBuiler()
	{
		ChanJoinBuilder _builder = new ChanJoinBuilder();
		ChanJoinMessage _msg    = _builder.build(USER_STRING+" JOIN "+TEST_CHANNEL);
		checkChannelAndUser(_msg.getFromUser(),_msg.getChannelName(),true);
	}
	
	/**
	 * Test to build a PART MESSAGE
	 */
	public void testChannelPartBuiler()
	{
		ChanJoinBuilder _builder = new ChanJoinBuilder();
		ChanJoinMessage _msg    = _builder.build(USER_STRING+" PART "+TEST_CHANNEL);
		checkChannelAndUser(_msg.getFromUser(),_msg.getChannelName(),true);
	}
	
	/**
	 * Test to build a PART MESSAGE
	 */
	public void testNoticeBuiler()
	{
		AbstractNoticeBuilder _builder = new AbstractNoticeBuilder()
		{
			@Override
			protected Set<Character> getChannelTypes()
			{
				return new LinkedHashSet<Character>() {{ add('#'); }};
			}
		};
		AbstractNotice _msg    = _builder.build("NOTICE Server Shit");
		assertEquals(_msg.getClass(),ServerNotice.class);
		
		_msg = _builder.build(USER_STRING+" NOTICE Something To An User");
		assertEquals(_msg.getClass(),UserNotice.class);
		assertEquals(TEST_USER,((UserNotice)_msg).getFromUser());
		assertEquals(TEST_USER.getNick()+"!"+TEST_USER.getIdent()+"@"+TEST_USER.getHostname()+" NOTICE Something To An User",((UserNotice)_msg).getText());
		
		_msg = _builder.build(USER_STRING+" NOTICE "+TEST_CHANNEL+" Something To a Chan");
		assertEquals(_msg.getClass(),ChannelNotice.class);
		checkChannelAndUser(((ChannelNotice)_msg).getFromUser(),((ChannelNotice)_msg).getChannelName(),false);
		assertEquals(TEST_USER.getNick()+"!"+TEST_USER.getIdent()+"@"+TEST_USER.getHostname()+" NOTICE " +TEST_CHANNEL+" Something To a Chan",((ChannelNotice)_msg).getText());
	}
	
	/**
	 * Test to build a PING MESSAGE
	 */
	public void testPingBuiler()
	{
		PingMessageBuilder _builder = new PingMessageBuilder();
		AbstractPingMessage _msg    = _builder.build("PING miguel:1234");
		assertEquals("1234",_msg.getReplyText());
	}

	
	/**
	 * Test to build a PRIVMSG MESSAGE
	 */
	public void testQuitMessageBuiler()
	{
		QuitMessageBuilder _builder = new QuitMessageBuilder();
		QuitMessage _msg    = _builder.build(USER_STRING+" QUIT :stfu message");
		assertEquals("stfu message",_msg.getQuitMsg());
		assertEquals(TEST_USER,_msg.getFromUser());
	}
	
	
	/**
	 * Test to build a TOPIC MESSAGE
	 */
	public void testTopicMessageBuiler()
	{
		TopicMessageBuilder _builder = new TopicMessageBuilder();
		TopicMessage _msg = _builder.build(USER_STRING+ " TOPIC " + TEST_CHANNEL + " :let's set that topic :D");
		checkChannelAndUser(ParseUtils.getUser(":" + _msg.getTopic().getSetBy()),_msg.getChannelName(),false);
		assertEquals(_msg.getChannelName(), TEST_CHANNEL);
		assertEquals(_msg.getTopic().getValue(),"let's set that topic :D");
		
	}
	
	/**
	 * Test to build a PRIVMSG MESSAGE
	 */
	public void testPrivateMessageBuiler()
	{
		//TODO i'm lazy :D
	}
	
	/**
	 * Test to build a SERVER MESSAGE
	 */
	public void testServerMessageBuiler()
	{
		//TODO i'm lazy :D
	}
	
	/**
	 * Test to build a MODE MESSAGE
	 */
	public void testChanModeBuiler()
	{
		//TODO i'm lazy :D
	}
	
}
