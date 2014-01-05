package com.ircclouds.irc.api.om;

import java.util.*;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;

/**
 * 
 * @author didry
 * 
 */
public class TopicMessageBuilder implements IBuilder<TopicMessage>
{
	@Override
	public TopicMessage build(String aMessage)
	{
		// user TOPIC #channel :topic
		int idx1 = aMessage.indexOf(' ');
		int idx2 = aMessage.indexOf(' ', idx1 + 1);
		int idx3 = aMessage.indexOf(' ', idx2 + 1);

		String _user = aMessage.substring(1, idx1);
		String _chan = aMessage.substring(idx2 + 1, idx3);
		String _topic = aMessage.substring(idx3 + 1, aMessage.length()).substring(1);

		return new TopicMessage(_chan, new WritableIRCTopic(_user, new Date(), _topic));
	}
}
