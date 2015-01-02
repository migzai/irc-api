package com.ircclouds.irc.api;

import java.util.*;

import org.slf4j.*;

import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;
import com.ircclouds.irc.api.filters.*;
import com.ircclouds.irc.api.listeners.*;

public final class MessageDispatcherImpl implements IMessageDispatcher
{
	private static Logger LOG = LoggerFactory.getLogger(MessageDispatcherImpl.class);
	
	private Map<MESSAGE_VISIBILITY, List<IMessageListener>> listenersMap = new HashMap<MESSAGE_VISIBILITY, List<IMessageListener>>();
	
	public MessageDispatcherImpl()
	{
		listenersMap.put(MESSAGE_VISIBILITY.PRIVATE, new ArrayList<IMessageListener>());
		listenersMap.put(MESSAGE_VISIBILITY.PUBLIC, new ArrayList<IMessageListener>());		
	}

	public void dispatch(IMessage aMessage, TargetListeners aTargetListeners)
	{
		if (aTargetListeners.getHowMany().equals(HowMany.ALL))
		{
			dispatchTo(aMessage, new ArrayList<IMessageListener>(listenersMap.get(MESSAGE_VISIBILITY.PUBLIC)));
		}
		else
		{
			dispatchTo(aMessage, aTargetListeners.getListeners());
		}
	}

	@Override
	public void dispatchToPrivateListeners(IMessage aMessage)
	{
		dispatchTo(aMessage, new ArrayList<IMessageListener>(listenersMap.get(MESSAGE_VISIBILITY.PRIVATE)));
	}	
	
	public void register(IMessageListener aListener, MESSAGE_VISIBILITY aVisibility)
	{
		listenersMap.get(aVisibility).add(aListener);
	}

	public void unregister(IMessageListener aListener)
	{
		listenersMap.get(MESSAGE_VISIBILITY.PRIVATE).remove(aListener);
		listenersMap.get(MESSAGE_VISIBILITY.PUBLIC).remove(aListener);
	}
	
	private void dispatchTo(IMessage aMessage, List<IMessageListener> aListeners)
	{
		for (IMessageListener _listener : aListeners)
		{
			//LOG.debug("Dispatching " + aMessage.getClass().getSimpleName() + " to " +  _listener.getClass().getSimpleName());
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
		if (aMessage instanceof ChanJoinMessage)
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
		else if (aMessage instanceof ChannelKick)
		{
			aListener.onChannelKick((ChannelKick) aMessage);
		}
		else if (aMessage instanceof ChannelPrivMsg)
		{
			aListener.onChannelMessage((ChannelPrivMsg) aMessage);
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
		else if (aMessage instanceof ServerNumericMessage)
		{
			aListener.onServerNumericMessage((ServerNumericMessage) aMessage);
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
		else if (aMessage instanceof ClientErrorMessage)
		{
			aListener.onClientError((ClientErrorMessage) aMessage);
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
