package com.sirma.itt.javacourse.chatapp;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

/**
 * Test class for {@link Server}.
 * 
 * @author Radoslav
 */
public class ServerTest {

	/**
	 * Starts the server and checks if it is running after its stopMethod is
	 * invoked
	 * 
	 * @throws IOException
	 *             Exception throw by the {@link Server}.
	 * @throws InterruptedException
	 *             Thrown by {link {@link Thread}.
	 */
	@Test
	public void testStop() throws IOException, InterruptedException {
		Server srv = new Server();
		srv.start();
		Thread.sleep(300);// wait a bit
		AtomicBoolean status = srv.getRunning();
		assertTrue(status.get());
		Thread.sleep(300);// wait a bit
		srv.stopServer();
		Thread.sleep(300);// wait a bit
		assertTrue(!status.get());
	}

}
