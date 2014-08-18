package com.sirma.itt.javacourse.chatapp;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sirma.itt.javacourse.chattapp.Request;
import com.sirma.itt.javacourse.chattapp.RequestProcessor;

/**
 * Thread that writes all data from the queue full of requests to the server's
 * output stream.
 * 
 * @author Radoslav
 */
public class ClientWriter extends Thread {

	private AtomicBoolean running;
	private Socket server;
	private BlockingQueue<Request> toServer;

	/**
	 * Initialises all required fields.
	 * 
	 * @param running
	 *            The state of the client.
	 * @param server
	 *            The server socket.
	 * @param toServer
	 *            The container where requests are stored.
	 */
	public ClientWriter(AtomicBoolean running, Socket server,
			BlockingQueue<Request> toServer) {
		this.running = running;
		this.server = server;
		this.toServer = toServer;
	}

	@Override
	public void run() {
		RequestProcessor processor = null;
		try {
			processor = new RequestProcessor(server.getOutputStream());
			while (running.get()) {
				if (!toServer.isEmpty()) {
					Request request = toServer.poll();
					processor.sendRequest(request);
				}
			}
		} catch (IOException e) {
			LogHandler.log("Server closed");
		}
		if (processor != null) {
			processor.closeStream();
		}
	}
}
