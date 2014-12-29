package com.ircclouds.irc.api.commands;

import java.util.*;

/**
 * CAP command to request 1 or more capabilities.
 *
 * @author Danny van Heumen
 */
public class CapReqCmd extends CapCmd {
	private final List<String> extensions = new LinkedList<String>();

	public CapReqCmd(String extension, String... extensions)
	{
		this.extensions.add(extension);
                this.extensions.addAll(Arrays.asList(extensions));
	}

	@Override
	public String asString()
	{
		final StringBuilder req = new StringBuilder("CAP REQ :");
		for (String ext : extensions) {
			req.append(ext).append(" ");
		}
		return req.append(CRNL).toString();
	}
}
