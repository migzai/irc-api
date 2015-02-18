package com.ircclouds.irc.api.om;

import java.util.*;

import org.slf4j.*;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

/**
 * 
 * @author miguel
 * 
 */

public abstract class AbstractMessageFactory
{
	private static final Logger LOG = LoggerFactory.getLogger(AbstractMessageFactory.class);
	
	private static final String PING_KEY = "PING";
	private static final String NOTICE_KEY = "NOTICE";
	private static final String PRIVATE_MESSAGE_KEY = "PRIVMSG";
	private static final String JOIN_KEY = "JOIN";
	private static final String PART_KEY = "PART";
	private static final String QUIT_MESSAGE_KEY = "QUIT";
	private static final String TOPIC_KEY = "TOPIC";
	private static final String NICK_KEY = "NICK";
	private static final String KICK_KEY = "KICK";
	private static final String MODE_KEY = "MODE";
	private static final String ERROR_KEY = "ERROR";
	
	private static final ServerMessageBuilder SERVER_MESSAGE_BUILDER = new ServerMessageBuilder();
	private static final TopicMessageBuilder TOPIC_MESSAGE_BUILDER = new TopicMessageBuilder();
	private static final NickMessageBuilder NICK_MESSAGE_BUILDER = new NickMessageBuilder();
	private static final KickMessageBuilder KICK_MESSAGE_BUILDER = new KickMessageBuilder();
	private static final ServerPingMessageBuilder SERVER_PING_MESSAGE_BUILDER = new ServerPingMessageBuilder();
	private static final ChanJoinBuilder CHAN_JOIN_BUILDER = new ChanJoinBuilder();
	private static final ChanPartBuilder CHAN_PART_BUILDER = new ChanPartBuilder();
	private static final QuitMessageBuilder QUIT_MESSAGE_BUILDER = new QuitMessageBuilder();
	private static final ErrorMessageBuilder ERROR_MESSAGE_BUILDER = new ErrorMessageBuilder();
	private static final UnknownMessageBuilder UNKNOWN_MESSAGE_BUILDER = new UnknownMessageBuilder();
	
	private final AbstractPrivateMessageBuilder PRIVATE_MESSAGE_BUILDER;
	private final AbstractNoticeBuilder NOTICE_BUILDER;
	private final AbstractChanModeBuilder CHAN_MODE_BUILDER;
			
	public AbstractMessageFactory()
	{
		PRIVATE_MESSAGE_BUILDER = new AbstractPrivateMessageBuilder()
		{
			@Override
			protected Set<Character> getChannelTypes()
			{
				return getIRCServerOptions().getChanTypes();
			}
		};
		NOTICE_BUILDER = new AbstractNoticeBuilder()
		{
			@Override
			protected Set<Character> getChannelTypes()
			{
				return getIRCServerOptions().getChanTypes();
			}		
		};
		CHAN_MODE_BUILDER = new AbstractChanModeBuilder()
		{
			@Override
			protected IRCServerOptions getIRCServerOptions()
			{
				return AbstractMessageFactory.this.getIRCServerOptions();
			}
		};
	}

	public IMessage build(String aMsg)
	{
		LOG.debug(aMsg);
		
		try
		{
			String _components[] = aMsg.split(" ");
			if (_components.length > 1)
			{
				String _msgType = _components[1];
				if (PING_KEY.equals(_components[0]))
				{
					return SERVER_PING_MESSAGE_BUILDER.build(aMsg);
				}
				else if (PRIVATE_MESSAGE_KEY.equals(_msgType))
				{
					return PRIVATE_MESSAGE_BUILDER.build(aMsg);
				}
				else if (NOTICE_KEY.equals(_msgType) || NOTICE_KEY.equals(_components[0]))
				{
					return NOTICE_BUILDER.build(aMsg);
				}
				else if (JOIN_KEY.equals(_msgType))
				{
					return CHAN_JOIN_BUILDER.build(aMsg);
				}
				else if (PART_KEY.equals(_msgType))
				{
					return CHAN_PART_BUILDER.build(aMsg);
				}
				else if (QUIT_MESSAGE_KEY.equals(_msgType))
				{
					return QUIT_MESSAGE_BUILDER.build(aMsg);
				}
				else if (TOPIC_KEY.equals(_msgType))
				{
					return TOPIC_MESSAGE_BUILDER.build(aMsg);
				}
				else if (NICK_KEY.equals(_msgType))
				{
					return NICK_MESSAGE_BUILDER.build(aMsg);
				}
				else if (KICK_KEY.equals(_msgType))
				{
					return KICK_MESSAGE_BUILDER.build(aMsg);
				}
				else if (MODE_KEY.equals(_msgType) && getIRCServerOptions().getChanTypes().contains(_components[2].charAt(0)))
				{
					return CHAN_MODE_BUILDER.build(aMsg);
				}
				else if (isNumeric(_msgType))
				{
					return SERVER_MESSAGE_BUILDER.build(aMsg);
				}
				else if (ERROR_KEY.equals(_components[0]))
				{
					return ERROR_MESSAGE_BUILDER.build(aMsg);
				}
			}

			return UNKNOWN_MESSAGE_BUILDER.build(aMsg);
		}
		catch (Exception aExc)
		{
			throw new IRCOMException(aExc);
		}
	}
	
	protected abstract IRCServerOptions getIRCServerOptions();
	
	private static boolean isNumeric(String aMsgType)
	{
		try
		{
			Integer.parseInt(aMsgType);
		}
		catch (NumberFormatException aExc)
		{
			return false;
		}

		return true;
	}
}
