package com.ircclouds.irc.api.negotiators;

import com.ircclouds.irc.api.negotiators.api.Relay;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Danny van Heumen
 */
public class SaslContextTest {

	public SaslContextTest() {
	}

	@Before
	public void setUp() {
	}

	@Test(expected = NullPointerException.class)
	@SuppressWarnings("ResultOfObjectAllocationIgnored")
	public void testConstructionNullRelay() {
		new SaslContext(null);
	}

	@Test
	@SuppressWarnings("ResultOfObjectAllocationIgnored")
	public void testConstruction(@Mocked Relay relay) {
		new SaslContext(relay);
	}

	@Test(expected = IllegalStateException.class)
	public void testNoInitiateConfirm(@Mocked Relay relay) {
		SaslContext context = new SaslContext(relay);
		context.confirm("+", "user", "pass", "role");
	}

	@Test(expected = IllegalStateException.class)
	public void testNoInitiateLoggedIn(@Mocked Relay relay) {
		SaslContext context = new SaslContext(relay);
		context.loggedIn();
	}

	@Test(expected = IllegalStateException.class)
	public void testNoInitiateSuccess(@Mocked Relay relay) {
		SaslContext context = new SaslContext(relay);
		context.success();
	}

	@Test
	public void testInit(@Mocked final Relay relay) {
		new Expectations() {
			{
				relay.send("AUTHENTICATE PLAIN");
			}
		};
		SaslContext context = new SaslContext(relay);
		context.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testNoConfirmLoggedIn(@Mocked Relay relay) {
		SaslContext context = new SaslContext(relay);
		context.init();
		context.loggedIn();
	}

	@Test(expected = IllegalStateException.class)
	public void testNoConfirmSuccess(@Mocked Relay relay) {
		SaslContext context = new SaslContext(relay);
		context.init();
		context.success();
	}

	@Test
	public void testConfirm(@Mocked final Relay relay) {
		new Expectations() {
			{
				relay.send("AUTHENTICATE PLAIN");
				relay.send("AUTHENTICATE amlsbGVzAGppbGxlcwBzZXNhbWU=");
			}
		};
		SaslContext context = new SaslContext(relay);
		context.init();
		context.confirm("+", "jilles", "jilles", "sesame");
	}

	@Test
	public void testConfirmWithMessageCutoff(@Mocked final Relay relay) {
		new Expectations() {
			{
				relay.send("AUTHENTICATE PLAIN");
				relay.send("AUTHENTICATE amlsbGVzamlsbGVzamlsbGVzamlsbGVzamlsbGVzamlsbGVzamlsbGVzamlsbGVzamlsbGVzamlsbGVzamlsbGVzamlsbGVzamlsbGVzamlsbGVzamlsbGVzamlsbGVzamlsbGVzamlsbGVzamlsbGVzamlsbGVzAGppbGxlc2ppbGxlc2ppbGxlc2ppbGxlc2ppbGxlc2ppbGxlc2ppbGxlc2ppbGxlc2ppbGxlc2ppbGxlcwBzZXNhbWVzZXNhbWVzZXNhbWVzZXNhbWVzZXNhbWVzZXNhbWVzZXNhbWVzZXNhbWVzZXNhbWVzZXNhbWVzZXNhbWVzZXNhbWVzZXNhbWVzZXNhbWVzZXNhbWVzZXNhbWVzZXNhbWVzZXNhbWV");
			}
		};
		SaslContext context = new SaslContext(relay);
		context.init();
		context.confirm("+", "jillesjillesjillesjillesjillesjillesjillesjillesjillesjillesjillesjillesjillesjillesjillesjillesjillesjillesjillesjilles", "jillesjillesjillesjillesjillesjillesjillesjillesjillesjilles", "sesamesesamesesamesesamesesamesesamesesamesesamesesamesesamesesamesesamesesamesesamesesamesesamesesamesesamesesamesesame");
	}

	@Test
	public void testConfirmLoggedIn(@Mocked final Relay relay) {
		new Expectations() {
			{
				relay.send("AUTHENTICATE PLAIN");
				relay.send("AUTHENTICATE amlsbGVzAGppbGxlcwBzZXNhbWU=");
			}
		};
		SaslContext context = new SaslContext(relay);
		context.init();
		context.confirm("+", "jilles", "jilles", "sesame");
		context.loggedIn();
	}

	@Test
	public void testConfirmSuccess(@Mocked final Relay relay) {
		new Expectations() {
			{
				relay.send("AUTHENTICATE PLAIN");
				relay.send("AUTHENTICATE amlsbGVzAGppbGxlcwBzZXNhbWU=");
			}
		};
		SaslContext context = new SaslContext(relay);
		context.init();
		context.confirm("+", "jilles", "jilles", "sesame");
		context.success();
	}

	@Test
	public void testFullyAuthenticated(@Mocked final Relay relay) {
		new Expectations() {
			{
				relay.send("AUTHENTICATE PLAIN");
				relay.send("AUTHENTICATE amlsbGVzAGppbGxlcwBzZXNhbWU=");
			}
		};
		SaslContext context = new SaslContext(relay);
		context.init();
		context.confirm("+", "jilles", "jilles", "sesame");
		context.loggedIn();
		context.success();
	}

	@Test(expected = IllegalStateException.class)
	public void testFailBeforeInitiation(@Mocked final Relay relay) {
		SaslContext context = new SaslContext(relay);
		context.fail();
	}

	@Test(expected = IllegalStateException.class)
	public void testAbortBeforeInitiation(@Mocked final Relay relay) {
		SaslContext context = new SaslContext(relay);
		context.abort();
	}

	@Test
	public void testAbortAfterInitiation(@Mocked final Relay relay) {
		new Expectations() {
			{
				relay.send("AUTHENTICATE PLAIN");
				relay.send("AUTHENTICATE *");
			}
		};
		SaslContext context = new SaslContext(relay);
		context.init();
		context.abort();
	}

	@Test
	public void testAbortAfterConfirmation(@Mocked final Relay relay) {
		new Expectations() {
			{
				relay.send("AUTHENTICATE PLAIN");
				relay.send("AUTHENTICATE amlsbGVzAGppbGxlcwBzZXNhbWU=");
				relay.send("AUTHENTICATE *");
			}
		};
		SaslContext context = new SaslContext(relay);
		context.init();
		context.confirm("+", "jilles", "jilles", "sesame");
		context.abort();
	}

	@Test
	public void testFailAfterSuccessfulAuthentication(@Mocked final Relay relay) {
		new Expectations() {
			{
				relay.send("AUTHENTICATE PLAIN");
				relay.send("AUTHENTICATE amlsbGVzAGppbGxlcwBzZXNhbWU=");
			}
		};
		SaslContext context = new SaslContext(relay);
		context.init();
		context.confirm("+", "jilles", "jilles", "sesame");
		context.loggedIn();
		context.success();
		context.fail();
	}

	@Test
	public void testAbortOnFailedAuthenticationMechanism(@Mocked final Relay relay) {
		new Expectations() {
			{
				relay.send("AUTHENTICATE PLAIN");
				relay.send("AUTHENTICATE *");
			}
		};
		SaslContext context = new SaslContext(relay);
		context.init();
		context.fail();
	}

	@Test
	public void testAbortOnFailedAuthentication(@Mocked final Relay relay) {
		new Expectations() {
			{
				relay.send("AUTHENTICATE PLAIN");
				relay.send("AUTHENTICATE amlsbGVzAGppbGxlcwBzZXNhbWU=");
				relay.send("AUTHENTICATE *");
			}
		};
		SaslContext context = new SaslContext(relay);
		context.init();
		context.confirm("+", "jilles", "jilles", "sesame");
		context.fail();
	}
}
