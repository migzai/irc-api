package com.ircclouds.irc.api.negotiators;

import com.ircclouds.irc.api.CapabilityNegotiator;
import com.ircclouds.irc.api.IRCApi;
import com.ircclouds.irc.api.commands.CapCmd;
import com.ircclouds.irc.api.commands.CapEndCmd;
import com.ircclouds.irc.api.commands.CapLsCmd;
import com.ircclouds.irc.api.commands.ICommand;
import com.ircclouds.irc.api.domain.messages.interfaces.IMessage;
import com.ircclouds.irc.api.listeners.VariousMessageListenerAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Composite Negotiator. A composite negotiator that will handle negotiator for
 * a provided list of capabilities.
 *
 * The composite negotiator is a stateful negotiator that will start with a
 * clean state upon initiation.
 *
 * FIXME Consider using a second collection and keep primary collection
 * "capabilities" as immutable reference.
 *
 * @author Danny van Heumen
 */
public class CompositeNegotiator extends VariousMessageListenerAdapter implements CapabilityNegotiator
{
	/**
	 * Logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(CompositeNegotiator.class);

	private static final Pattern CAPABILITY_LS = Pattern.compile("\\sCAP\\s+([^\\s]+)\\s+LS\\s+:([\\w-_]+(?:\\s+[\\w-_]+)*)\\s*$", 0);
	private static final Pattern CAPABILITY_ACK = Pattern.compile("\\sCAP\\s+([^\\s]+)\\s+ACK\\s+:([\\w-_]+(?:\\s+[\\w-_]+)*)\\s*$", 0);
	private static final Pattern CAPABILITY_NAK = Pattern.compile("\\sCAP\\s+([^\\s]+)\\s+NAK");

	/**
	 * List of requested capabilities.
	 */
	private final List<Capability> capabilities;
	/**
	 * Host instance used for feedback during the negotiation process.
	 */
	private final Host host;

	/**
	 * List of requested capabilities. (Starts empty.) The list will contain all
	 * capabilities that have been requested from the server.
	 */
	private final LinkedList<Capability> requested = new LinkedList<Capability>();
	/**
	 * List of acknowledged capabilities. (Starts empty.) The list will contain
	 * all capabilities that have been acknowledged by the server.
	 */
	private final LinkedList<Capability> acknowledged = new LinkedList<Capability>();

	/**
	 * The IRC-API instance.
	 */
	private IRCApi irc;

	public CompositeNegotiator(List<Capability> capabilities, Host host)
	{
		if (capabilities == null)
		{
			throw new NullPointerException("capabilities");
		}
		this.capabilities = Collections.unmodifiableList(verify(capabilities));
		this.host = host;
	}

	/**
	 * Verify all capabilities in the list for correct values.
	 *
	 * @param caps the list of capabilities
	 * @return Returns the list of capabilities if it is verified correct.
	 * @throws IllegalArgumentException Throws an exception in case bad values
	 * are found.
	 */
	private static List<Capability> verify(List<Capability> caps)
	{
		for (Capability cap : caps)
		{
			if (cap.getId() == null || cap.getId().isEmpty())
			{
				throw new IllegalArgumentException("capability " + cap.getId() + " cannot have null or empty id");
			}
//			if (cap.needsConversation() && !(cap instanceof ConversationCapability))
//			{
//				throw new IllegalArgumentException("capability " + cap.getId() + " requests conversation with server, but does not implement interface ConversationCapability");
//			}
		}
		return caps;
	}

	@Override
	public CapCmd initiate(IRCApi irc)
	{
		if (irc == null)
		{
			throw new IllegalArgumentException("irc instance is required");
		}
		this.irc = irc;
		this.requested.clear();
		this.acknowledged.clear();
		return new CapLsCmd();
	}

	@Override
	public void onMessage(IMessage msg)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("SERVER: " + msg.asRaw());
		}
		final String rawmsg = msg.asRaw();
		final Matcher capLs = CAPABILITY_LS.matcher(rawmsg);
		final Matcher capAck = CAPABILITY_ACK.matcher(rawmsg);
		final Matcher capNak = CAPABILITY_NAK.matcher(rawmsg);
		try
		{
			// FIXME support sticky capabilities
			// FIXME support sticky capabilities that don't match the request
			if (capLs.find())
			{
				final LinkedList<Cap> responseCaps = parseResponseCaps(capLs.group(2));
				final List<Capability> reject = unsupportedCapabilities(responseCaps);
				feedbackRejection(reject);
				final List<Capability> request = requestedCapabilities(reject);
				if (request.isEmpty())
				{
					// there's nothing left to request, so finish capability negotiation
					send(this.irc, new CapEndCmd());
				}
				else
				{
					this.requested.addAll(request);
					sendCapabilityRequest();
				}
			}
			else if (capAck.find())
			{
				final LinkedList<Cap> responseCaps = parseResponseCaps(capAck.group(2));
				final List<Capability> confirms = acknowledgeCapabilities(responseCaps);
				if (!confirms.isEmpty())
				{
					// According to irc.atheme.org/ircv3 server will remain
					// silent after sending client ACKs. So fire and forget.
					sendCapabilityConfirmation(confirms);
					this.acknowledged.addAll(confirms);
					feedbackAcknowledgements(confirms);
				}
				// FIXME wait for multi-response acks?
				// FIXME start capability conversations
				send(this.irc, new CapEndCmd());
			}
			else if (capNak.find())
			{
				LOG.error("Capability request NOT Acknowledged: " + rawmsg + " (this may be due to inconsistent server responses)");
				send(this.irc, new CapEndCmd());
			}
		}
		catch (RuntimeException e)
		{
			LOG.error("Error occurred during CAP negotiation. Prematurely ending CAP negotiation phase and continuing IRC registration as is.", e);
			this.irc.rawMessage(new CapEndCmd().asString());
		}
	}

	/**
	 * Parse response capabilities from IRC server response.
	 *
	 * @param responseText the response text
	 * @return Returns list of Cap instances.
	 */
	static LinkedList<Cap> parseResponseCaps(final String responseText)
	{
		final LinkedList<Cap> caps = new LinkedList<Cap>();
		for (String capdesc : responseText.split("\\s+"))
		{
			if (capdesc.isEmpty())
			{
				continue;
			}
			caps.add(new Cap(capdesc));
		}
		return caps;
	}

	/**
	 * Extract capabilities unsupported by the IRC server.
	 *
	 * @param capLs the CAP LS server response
	 * @return Returns the capabilities that are not supported by the IRC
	 * server.
	 */
	private List<Capability> unsupportedCapabilities(final LinkedList<Cap> capLs)
	{
		// find all supported capabilities
		final ArrayList<Capability> found = new ArrayList<Capability>();
		for (Capability request : this.capabilities)
		{
			for (Cap available : capLs)
			{
				if (request.getId().equals(available.id))
				{
					if (request.enable() == available.isEnabled() || !available.isMandatory())
					{
						// Only if wishes match server expectations, will we
						// consider it found. So if we wish to disable a feature
						// and it is mandatory, then we consider it unsupported.
						found.add(request);
					}
					// in any case we found the capability, so stop looking for it
					break;
				}
			}
		}
		// compute unsupported capabilities
		final ArrayList<Capability> unsupported = new ArrayList<Capability>(this.capabilities);
		unsupported.removeAll(found);
		if (LOG.isTraceEnabled())
		{
			LOG.trace("Supported capabilities: " + repr(found));
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Unsupported capabilities: " + repr(unsupported));
		}
		return unsupported;
	}

	/**
	 * Extract capabilities to request.
	 *
	 * @param unsupported list of unsupported capabilities that should not be
	 * requested
	 * @return Returns list of capabilities that must be requested.
	 */
	private List<Capability> requestedCapabilities(final List<Capability> unsupported)
	{
		final ArrayList<Capability> requested = new ArrayList<Capability>(this.capabilities);
		requested.removeAll(unsupported);
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Requesting capabilities: " + repr(requested));
		}
		return requested;
	}

	/**
	 * Send capability request.
	 */
	private void sendCapabilityRequest()
	{
		final StringBuilder requestLine = new StringBuilder("CAP REQ :");
		for (Capability cap : this.requested)
		{
			requestLine.append(repr(cap)).append(' ');
		}
		send(this.irc, requestLine.toString());
	}

	/**
	 * Checks server response for acknowledged capabilities.
	 *
	 * @param capAck all capabilities acknowledged by the IRC server
	 * @return Returns the list of capabilities that need to be confirmed.
	 */
	private List<Capability> acknowledgeCapabilities(final LinkedList<Cap> capAck)
	{
		final LinkedList<Capability> acks = new LinkedList<Capability>();
		final LinkedList<Capability> needsConfirmation = new LinkedList<Capability>();
		for (Capability request : this.capabilities)
		{
			for (Cap cap : capAck)
			{
				if (!request.getId().equals(cap.id))
				{
					continue;
				}
				if (request.enable() == cap.enabled)
				{
					if (cap.requiresAck)
					{
						needsConfirmation.add(request);
					}
					else
					{
						acks.add(request);
					}
				}
				else
				{
					// FIXME feedback inverse capability as rejected?
					LOG.warn("Inverse of requested state was acknowledged by IRC server: " + cap.id + "(" + cap.enabled + ")");
				}
			}
			if (!acks.contains(request) && !needsConfirmation.contains(request)) {
				// FIXME feedback lost capability as rejected?
				LOG.warn("Capability " + request.getId() + " was not acknowledged by IRC server. (Lost)");
			}
		}
		this.acknowledged.addAll(acks);
		feedbackAcknowledgements(acks);
		return needsConfirmation;
	}

	/**
	 * Send ACK for capabilities that need confirmation.
	 *
	 * @param confirms list of capabilities that require client confirmation
	 */
	private void sendCapabilityConfirmation(final List<Capability> confirms)
	{
		final StringBuilder confirmation = new StringBuilder("CAP ACK :");
		for (Capability cap : confirms)
		{
			confirmation.append('~').append(repr(cap)).append(' ');
		}
		send(this.irc, confirmation.toString());
	}

	/**
	 * Call host with feedback for acknowledged capabilities.
	 *
	 * @param caps the capabilities that are acknowledged
	 */
	private void feedbackAcknowledgements(List<Capability> caps)
	{
		for (Capability cap : caps)
		{
			try
			{
				this.host.acknowledge(cap);
			}
			catch (RuntimeException e)
			{
				LOG.warn("BUG: host threw a runtime exception while processing acknowledgement.", e);
			}
		}
	}

	/**
	 * Call host with feedback for rejected capabilities.
	 *
	 * @param caps the capabilities that are rejected
	 */
	private void feedbackRejection(List<Capability> caps)
	{
		for (Capability cap : caps)
		{
			try
			{
				this.host.reject(cap);
			}
			catch (RuntimeException e)
			{
				LOG.warn("BUG: host threw a runtime exception while processing rejection.", e);
			}
		}
	}

	/**
	 * Generate textual representation of capability.
	 *
	 * @param cap the capability
	 * @return Returns the textual representation of the capability.
	 */
	private String repr(Capability cap)
	{
		if (cap.enable())
		{
			return cap.getId();
		}
		else
		{
			return "-" + cap.getId();
		}
	}

	/**
	 * Representation of a list of capabilities.
	 *
	 * @param caps the list of capabilities
	 * @return Returns the string representation of the list.
	 */
	private String repr(List<Capability> caps)
	{
		StringBuilder line = new StringBuilder("{");
		for (Capability cap : caps) {
			line.append(repr(cap)).append(", ");
		}
		line.append("}");
		return line.toString();
	}

	private static void send(final IRCApi irc, final ICommand cmd)
	{
		send(irc, cmd.asString());
	}

	private static void send(final IRCApi irc, final String msg)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("CLIENT: " + msg);
		}
		irc.rawMessage(msg);
	}

	static class Cap
	{
		/**
		 * Capability is enabled.
		 */
		private final boolean enabled;

		/**
		 * Capability still requires acknowledgement from client.
		 */
		private final boolean requiresAck;

		/**
		 * Capability is mandatory, state cannot be changed.
		 */
		private final boolean mandatory;

		/**
		 * ID of capability for negotiation.
		 */
		private final String id;

		/**
		 * Raw capability name (including modifiers) that originates from the
		 * (raw) server response.
		 *
		 * @param response the (raw) server response
		 */
		private Cap(final String response)
		{
			int start = 0;
			boolean enabled = true, requiresAck = false, mandatory = false;

			modifierLoop:
			for (int i = 0; i < response.length(); i++)
			{
				switch (response.charAt(i))
				{
				case '-':
					enabled = false;
					break;
				case '~':
					requiresAck = true;
					break;
				case '=':
					mandatory = true;
					break;
				default:
					start = i;
					break modifierLoop;
				}
			}
			this.id = response.substring(start);
			this.enabled = enabled;
			this.requiresAck = requiresAck;
			this.mandatory = mandatory;
		}

		String getId() {
			return this.id;
		}

		boolean isEnabled() {
			return this.enabled;
		}

		boolean isRquiresAck() {
			return this.requiresAck;
		}

		boolean isMandatory() {
			return this.mandatory;
		}
	}

	public static interface Host
	{
		/**
		 * Acknowledge capability is accepted by IRC server to host instance.
		 *
		 * @param cap the capability that has been acknowledged
		 */
		void acknowledge(Capability cap);

		/**
		 * Reject capability feedback to the host instance.
		 *
		 * @param cap the capability that has been rejected
		 */
		void reject(Capability cap);
	}

	/**
	 * Interface for capability that needs to be negotiated with an
	 * IRCv3-capable IRC server.
	 */
	public static interface Capability
	{
		/**
		 * Get the id with which to negotiate for this capability.
		 *
		 * The ID must not be null or empty.
		 *
		 * @return Returns the capability id to use in negotiation.
		 */
		String getId();

		/**
		 * Get the resulting state that is requested by this capability.
		 *
		 * @return Returns true to negotiate capability to be enabled, false to
		 * negotiate capability to be disabled.
		 */
		boolean enable();

//		/**
//		 * Indicates that a capability needs conversation time with the server.
//		 *
//		 * FIXME correct interface, extend description
//		 * If this indicator is true, it is assumed that ConversationCapability interface is implemented.
//		 *
//		 * @return Returns true if capability needs conversation time with server, or false if not.
//		 */
//		boolean needsConversation();
	}

//	public static interface ConversationCapability
//	{
//		// FIXME define conversation methods
//	}
}
