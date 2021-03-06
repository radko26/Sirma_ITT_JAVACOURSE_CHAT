package com.sirma.itt.javacourse.chatapp;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sirma.itt.javacourse.chatapp.Request;
import com.sirma.itt.javacourse.chatapp.RequestProcessor;

/**
 * Thread that listens for incoming requests from the server.
 * 
 * @author Radoslav
 */
public class ClientReader extends Thread {

	private AtomicBoolean running;
	private Socket server;
	private BlockingQueue<Request> fromServer;
	private AtomicBoolean isServerRunning;

	/**
	 * Initialises fields.
	 * 
	 * @param running
	 *            The state of the client.
	 * @param server
	 *            Socket representing the server.
	 * @param fromServer
	 *            Queue that holds requests from server.
	 */
	public ClientReader(AtomicBoolean running, AtomicBoolean isServerRunning,
			Socket server, BlockingQueue<Request> fromServer) {
		this.running = running;
		this.isServerRunning = isServerRunning;
		this.server = server;
		this.fromServer = fromServer;
	}

	@Override
	public void run() {
		RequestProcessor reader = null;
		try {
			reader = new RequestProcessor(server.getInputStream());
			Request message;
			while (running.get()) {
				message = reader.receiveRequest();
				fromServer.add(message);
			}
		} catch (IOException | ClassNotFoundException e) {
			LogHandler.log("Server is closed");
			isServerRunning.set(false);
		}
		if (reader != null) {
			reader.closeStream();
		}
	}
}
