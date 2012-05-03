package com.ircclouds.irc.api;

import java.util.*;

import org.slf4j.*;

import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;
import com.ircclouds.irc.api.filters.*;
import com.ircclouds.irc.api.listeners.*;

public final class MessagesDispatcherImpl implements IMessagesDispatcher
{
	private static Logger LOG = LoggerFactory.getLogger(MessagesDispatcherImpl.class);
	
	private Map<ListenerLevel, List<IMessageListener>> listenersMap = new HashMap<ListenerLevel, List<IMessageListener>>();
	
	public MessagesDispatcherImpl()
	{
		listenersMap.put(ListenerLevel.PRIVATE, new ArrayList<IMessageListener>());
		listenersMap.put(ListenerLevel.PUBLIC, new ArrayList<IMessageListener>());		
	}

	public void dispatch(IMessage aMessage, TargetListeners aTargetListeners)
	{
		if (aTargetListeners.getHowMany().equals(HowMany.ALL))
		{
			dispatchTo(aMessage, new ArrayList<IMessageListener>(listenersMap.get(ListenerLevel.PUBLIC)));
		}
		else
		{
			dispatchTo(aMessage, aTargetListeners.getListeners());
		}
	}

	@Override
	public void dispatchToPrivateListeners(IMessage aMessage)
	{
		dispatchTo(aMessage, new ArrayList<IMessageListener>(listenersMap.get(ListenerLevel.PRIVATE)));
	}	
	
	public void register(IMessageListener aListener, ListenerLevel aLevel)
	{
		listenersMap.get(aLevel).add(aListener);
	}

	public void unregister(IMessageListener aListener)
	{
		listenersMap.get(ListenerLevel.PUBLIC).remove(aListener);
		listenersMap.get(ListenerLevel.PUBLIC).remove(aListener);
	}
	
	private void dispatchTo(IMessage aMessage, List<IMessageListener> aListeners)
	{
		for (IMessageListener _listener : aListeners)
		{
			LOG.debug("Dispatching " + aMessage.getClass().getSimpleName() + " to " +  _listener.getClass().getSimpleName());
			try
			{
				if (_listener instanceof IVariousMessageListener)
				{
					dispatchVarious((IVariousMessageListener) _listener, aMessage);
				}
				else
				{
					_listener.onMessage(aMessage);
				}
			}
			catch (Exception aExc)
			{
				LOG.error("", aExc);
			}			
		}		
	}

	private void dispatchVarious(IVariousMessageListener aListener, IMessage aMessage)
	{
		if (aMessage instanceof ChannelPrivMsg)
		{
			aListener.onChannelMessage((ChannelPrivMsg) aMessage);
		}
		else if (aMessage instanceof ChanJoinMessage)
		{
			aListener.onChannelJoin((ChanJoinMessage) aMessage);
		}
		else if (aMessage instanceof ChanPartMessage)
		{
			aListener.onChannelPart((ChanPartMessage) aMessage);
		}
		else if (aMessage instanceof ChannelNotice)
		{
			aListener.onChannelNotice((ChannelNotice) aMessage);
		}
		else if (aMessage instanceof ChannelActionMsg)
		{
			aListener.onChannelAction((ChannelActionMsg) aMessage);
		}
		else if (aMessage instanceof ChannelKickMsg)
		{
			aListener.onChannelKickMessage((ChannelKickMsg) aMessage);
		}
		else if (aMessage instanceof TopicMessage)
		{
			aListener.onTopicChange((TopicMessage) aMessage);
		}
		else if (aMessage instanceof UserPrivMsg)
		{
			if (aMessage instanceof UserVersion)
			{
				aListener.onUserVersion((UserVersion) aMessage);
			}
			else if (aMessage instanceof UserPing)
			{
				aListener.onUserPing((UserPing) aMessage);
			}
			else if (aMessage instanceof UserActionMsg)
			{
				aListener.onUserAction((UserActionMsg) aMessage);
			}
			else
			{
				aListener.onUserPrivMessage((UserPrivMsg) aMessage);
			}
		}
		else if (aMessage instanceof UserNotice)
		{
			aListener.onUserNotice((UserNotice) aMessage);
		}
		else if (aMessage instanceof ServerMessage)
		{
			aListener.onServerMsg((ServerMessage) aMessage);
		}
		else if (aMessage instanceof ServerNotice)
		{
			aListener.onServerNotice((ServerNotice) aMessage);
		}
		else if (aMessage instanceof NickMessage)
		{
			aListener.onNickChange((NickMessage) aMessage);
		}
		else if (aMessage instanceof QuitMessage)
		{
			aListener.onUserQuit((QuitMessage) aMessage);
		}
		else if (aMessage instanceof ErrorMessage)
		{
			aListener.onError((ErrorMessage) aMessage);
		}
		else if (aMessage instanceof ServerAuth)
		{
			aListener.onServerAuth((ServerAuth) aMessage);
		}
		else if (aMessage instanceof ChannelModeMessage)
		{
			aListener.onChannelMode((ChannelModeMessage) aMessage);
		}
		else if (aMessage instanceof ServerPing)
		{
			aListener.onServerPing((ServerPing) aMessage);
		}
		else
		{
			aListener.onMessage(aMessage);
		}
	}
}
