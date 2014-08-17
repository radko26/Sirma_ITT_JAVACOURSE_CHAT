package com.sirma.itt.javacourse.chatapp;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;


/**
 * 
 * 
 * @author Radoslav
 */
 
public class ClientTest {

	/**
	 * @throws IOException 
	 * @throws InterruptedException 
	 * 
	 */
	@Test
	public void testSend() throws IOException, InterruptedException {
		Thread server = new Thread(new Server());
		server.start();
		Thread.currentThread().sleep(300 );
		new Thread(new Client(new AtomicBoolean(true))).start();
		
	}

}
