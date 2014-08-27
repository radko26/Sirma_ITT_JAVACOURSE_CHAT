package com.sirma.itt.javacourse.chatapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
	 *             Thrown by test threads.
	 * 
	 */
	@Test
	public void testAccept() throws IOException, InterruptedException {
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

	/**
	 * Tries to connect two clients with same name also known as 'radko'
	 * 
	 * @throws InterruptedException
	 *             Thrown by {@link Server} and {@link Client}.
	 * @throws IOException
	 *             Thrown by {@link Server} and {@link Client}.
	 */
	@Test
	public void testAuth() throws InterruptedException, IOException {
		Server srv = new Server();
		srv.start();
		Thread.sleep(300);
		Client client = new Client(new AtomicBoolean(true));
		Thread clientThread = new Thread(client);
		clientThread.start();
		Thread.sleep(100);
		Thread client2Thread = new Thread(client);
		client2Thread.start();
		Thread.sleep(100);
		assertTrue(!client2Thread.isAlive());
		Thread.sleep(300);
		assertEquals(1, srv.getConnected().size());
		client.stopClientRunning();
		srv.stopServer();
		Thread.sleep(50);
	}
}
