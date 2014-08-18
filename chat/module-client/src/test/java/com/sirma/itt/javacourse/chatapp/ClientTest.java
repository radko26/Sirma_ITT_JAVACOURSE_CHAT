package com.sirma.itt.javacourse.chatapp;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

/**
 * 
 * Test class for {@link Client}.
 * 
 * @author Radoslav
 */

public class ClientTest {

	/**
	 * Starts server and client without GUI and checks if the clients has
	 * connected.
	 * 
	 * @throws IOException
	 *             Thrown by {@link Server} and {@link Client}.
	 * @throws InterruptedException
	 *             Thrown by {@link Server} and {@link Client}.
	 * @throws IllegalAccessException
	 *             Thrown by {@link Server} and {@link Client}.
	 * @throws IllegalArgumentException
	 *             Thrown by {@link Server} and {@link Client}.
	 * 
	 */
	@Test
	public void testAccept() throws IOException, InterruptedException,
			IllegalArgumentException, IllegalAccessException {
		Server srv = new Server();
		srv.start();
		Thread.sleep(300);
		Client client = new Client(new AtomicBoolean(true));
		Thread clientTread = new Thread(client);
		clientTread.start();
		Thread.sleep(300);
		assertEquals(1, srv.getConnected().size());
		client.stopClientRunning();
		srv.stopServer();
		Thread.sleep(50);
	}

}
