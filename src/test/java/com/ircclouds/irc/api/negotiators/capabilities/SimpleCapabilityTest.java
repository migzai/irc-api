package com.ircclouds.irc.api.negotiators.capabilities;

import com.ircclouds.irc.api.negotiators.api.Relay;
import mockit.Mocked;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Danny van Heumen
 */
public class SimpleCapabilityTest {
	
	public SimpleCapabilityTest() {
	}
	
	@Before
	public void setUp() {
	}

	@Test(expected = NullPointerException.class)
	public void testConstructingNullCapabilityId() {
		new SimpleCapability(null);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructingNullCapabilityIdEnabled() {
		new SimpleCapability(null, true);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructingNullCapabilityIdDisabled() {
		new SimpleCapability(null, false);
	}

	@Test
	public void testSimpleCapability() {
		SimpleCapability cap = new SimpleCapability("away-notify");
		assertTrue(cap.enable());
		assertEquals("away-notify", cap.getId());
	}

	@Test
	public void testSimpleCapabilityEnabled() {
		SimpleCapability cap = new SimpleCapability("away-notify", true);
		assertTrue(cap.enable());
		assertEquals("away-notify", cap.getId());
	}

	@Test
	public void testSimpleCapabilityDisabled() {
		SimpleCapability cap = new SimpleCapability("away-notify", false);
		assertFalse(cap.enable());
		assertEquals("away-notify", cap.getId());
	}

	@Test
	public void testSimpleCapabilityConversation(@Mocked final Relay relay) {
		SimpleCapability cap = new SimpleCapability("away-notify");
		assertFalse(cap.converse(relay, null));
	}
}
