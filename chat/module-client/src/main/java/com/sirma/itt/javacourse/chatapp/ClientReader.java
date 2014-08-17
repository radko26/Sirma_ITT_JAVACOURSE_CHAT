package com.sirma.itt.javacourse.chatapp;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sirma.itt.javacourse.chattapp.Request;
import com.sirma.itt.javacourse.chattapp.RequestProcessor;

/**
 * Thread that listens for incoming requests from the server.
 * 
 * @author Radoslav
 */
public class ClientReader extends Thread {

	private AtomicBoolean running;
	private Socket server;
	private BlockingQueue<Request> fromServer;

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
	public ClientReader(AtomicBoolean running, Socket server,
			BlockingQueue<Request> fromServer) {
		this.running = running;
		this.server = server;
		this.fromServer = fromServer;
	}

	@Override
	public void run() {
		try {
			RequestProcessor reader = new RequestProcessor(
					server.getInputStream());
			Request message;
			while (running.get()) {
				message = reader.receiveRequest();
				System.out.println("prochetoh");
				fromServer.add(message);
				System.out.println(fromServer.size());
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Server is closed");
		}

	}
}
