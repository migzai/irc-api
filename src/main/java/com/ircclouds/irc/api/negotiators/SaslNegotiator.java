package com.ircclouds.irc.api.negotiators;

import java.io.*;

import org.apache.commons.codec.binary.*;

import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.commands.*;
import com.ircclouds.irc.api.domain.messages.interfaces.*;

public class SaslNegotiator implements CapabilityNegotiator {
	private IRCApi irc;
	
	private final String user;
	private final String pass;
	private final String autzid;

	public SaslNegotiator(final String user, final String pass, final String authzid)
	{
		if (user == null)
		{
			throw new IllegalArgumentException("user cannot be null");
		}
		this.user = user;
		if (pass == null)
		{
			throw new IllegalArgumentException("pass cannot be null");
		}
		this.pass = pass;
		this.autzid = authzid;
	}

	@Override
	public CapCmd initiate(IRCApi irc)
	{
		this.irc = irc;
		return new CapReqCmd("sasl");
	}

	@Override
	public void onMessage(IMessage msg)
	{
		if (msg.asRaw().contains("CAP * ACK :sasl"))
		{
			this.irc.rawMessage("AUTHENTICATE PLAIN");
		}
		else if (msg.asRaw().contains("AUTHENTICATE +"))
		{
			String challenge = encode(this.autzid, this.user, this.pass);
			System.out.println("Response: " + challenge);
			this.irc.rawMessage("AUTHENTICATE " + challenge);
		}
		else if (msg.asRaw().contains(":SASL authentication successful"))
		{
			this.irc.rawMessage("CAP END");
		}
		else
		{
			// IGNORING currently ...
		}
	}

	private static String encode(final String authzid, final String user,
			final String pass) {
		final StringBuilder response = new StringBuilder();
		if (authzid != null) {
			response.append(authzid);
		}
		response.append('\u0000').append(user).append('\u0000').append(pass);
		try {
			return Base64.encodeBase64String(response.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Unsupported encoding specified.", e);
		}
	}
}
