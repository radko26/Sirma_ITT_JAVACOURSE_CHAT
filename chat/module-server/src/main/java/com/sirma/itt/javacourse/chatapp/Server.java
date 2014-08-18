package com.sirma.itt.javacourse.chatapp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Radoslav
 */
public class Server extends Thread {
	private static final int PORT = 7001;
	private ServerSocket server;
	private Socket client;
	private AtomicBoolean running = new AtomicBoolean(true);
	private List<ClientHandleThread> connected = Collections
			.synchronizedList(new ArrayList<ClientHandleThread>());

	/**
	 * Initialises {@link ServerSocket}.
	 * 
	 * @throws IOException
	 *             If the port is not available.
	 */
	public Server() throws IOException {
		server = new ServerSocket(PORT);
	}

	/**
	 * Stops the server.
	 * 
	 * @throws IOException
	 *             If server cannot be closed.
	 */
	public void stopServer() throws IOException {
		running.set(false);
			server.close();
	}

	@Override
	public void run() {
		while (running.get()) {
			try {
				client = server.accept();
				new ClientHandleThread(client, running, connected).start();
			} catch (IOException e) {
				running.set(false);
			}
		}
	}

}
