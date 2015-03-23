package com.ircclouds.irc.api.negotiators.capabilities;

import com.ircclouds.irc.api.negotiators.CompositeNegotiator;
import com.ircclouds.irc.api.negotiators.api.Relay;

/**
 * A generic implementation for a simple capability used for negotiation. This
 * implementation does not support custom IRC server conversation and so can
 * only be used in order to negotiate capabilities that do not require
 * conversation with the IRC server.
 *
 * @author Danny van Heumen
 */
public class SimpleCapability implements CompositeNegotiator.Capability
{
	/**
	 * The capability id.
	 */
	private final String id;

	/**
	 * The required capability state: enabled or disabled.
	 */
	private final boolean enable;

	/**
	 * Create a simple capability for negotiation that will be enabled by
	 * default.
	 *
	 * @param id the capability id (cannot be null)
	 */
	public SimpleCapability(final String id)
	{
		this(id, true);
	}

	/**
	 * Create a simple capability for negotiation. A second parameter is
	 * provided to express the wish to either negotiate for enabling or
	 * disabling.
	 *
	 * @param id the capability id
	 * @param enable <tt>true</tt> to negotiate for enabling, <tt>false</tt> to
	 * negotiate for disabling
	 */
	public SimpleCapability(final String id, final boolean enable)
	{
		if (id == null)
		{
			throw new NullPointerException();
		}
		this.id = id;
		this.enable = enable;
	}

	@Override
	public String getId()
	{
		return this.id;
	}

	@Override
	public boolean enable()
	{
		return this.enable;
	}

	@Override
	public boolean converse(Relay relay, String msg)
	{
		return false;
	}
}
