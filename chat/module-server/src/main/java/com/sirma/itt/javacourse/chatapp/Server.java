package com.sirma.itt.javacourse.chatapp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread that starts the {@link ServerSocket}.
 * @author Radoslav
 */
public class Server extends Thread {
	private static int PORT;
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
		setConnectionProperties();
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

	/**
	 * Getter for the running status variable.
	 * 
	 * @return The variable.
	 * 
	 */
	public AtomicBoolean getRunning() {
		return running;
	}

	/**
	 * Getter for the list of connected users.
	 * 
	 * @return The list.
	 */
	public synchronized List<ClientHandleThread> getConnected() {
		return connected;
	}

	/**
	 * Loads the property file and sets the host and the port.
	 * 
	 * @throws IOException
	 *             For any problems with the input stream.
	 */
	private void setConnectionProperties() throws IOException {
		Properties properties = new Properties();
		properties
				.load(this.getClass().getResourceAsStream("setup.properties"));
		PORT = Integer.valueOf(properties.getProperty("port", "7001"));
		ContentLanguageManager.setLanguage(properties.getProperty("lang"));
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
