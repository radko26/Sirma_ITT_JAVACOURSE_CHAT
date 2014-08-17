package com.sirma.itt.javacourse.chatapp;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sirma.itt.javacourse.chattapp.Request;
import com.sirma.itt.javacourse.chattapp.RequestProcessor;

/**
 * TODO
 * 
 * @author Radoslav
 */
public class ClientThread extends Thread {
	private Socket client;
	private AtomicBoolean running;
	private List<ClientThread> connected;
	private RequestProcessor processor = null;

	/**
	 * 
	 * @param client
	 * @param running
	 * @param connected
	 */
	public ClientThread(Socket client, AtomicBoolean running,
			List<ClientThread> connected) {
		this.client = client;
		this.running = running;
		this.connected = connected;
	}

	@Override
	public void run() {

		try {
			processor = new RequestProcessor(client.getInputStream(),
					client.getOutputStream());
			Request userAuth = processor.receiveRequest();
			if (checkUsernameAuth(userAuth)) {
				String msg = new StringBuilder(userAuth.getContent()).append(
						" has connected").toString();
				broadcastToOthers(new Request().setType(Request.MESSAGE)
						.setContent(msg));
				LogHandler.log(msg);
				connected.add(this);
				processor.sendRequest(new Request().setType(Request.LOGIN_AUTH).setSuccessful(true));
				LogHandler.log("New client with " + client.getLocalAddress());
			} else {
				processor.sendRequest(new Request().setType(Request.LOGIN_AUTH).setSuccessful(false));
				processor.closeStream();
				client.close();
				
			}
			while (running.get()) {
				Request msg = processor.receiveRequest();
				processor.sendRequest(msg);
				LogHandler.log("izprashtam" + msg.getContent());
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("error s io v clientThread-a");// TODO
		} finally {
			if (processor != null) {
				processor.closeStream();
			}
		}
	}

	/**
	 * Checks whether the given username is valid.
	 * 
	 * @param loginAuth
	 *            The username.
	 * @return True if it is correct, otherwise false.
	 */
	private boolean checkUsernameAuth(Request loginAuth) {
		String username = loginAuth.getContent();
		int type = loginAuth.getType();
		if (type != Request.LOGIN_AUTH) {
			return false;
		} else if (username.contains("[") || username.contains("]")) {
			return false;
		} else if (!isUnique(username)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Checks if this username is unique.
	 * 
	 * @param username
	 *            The username,
	 * @return True if it is and flase if it is not.
	 */
	private boolean isUnique(String username) {
		return true;
	}

	/**
	 * 
	 * @param request
	 * @throws IOException
	 */
	private void broadcastToOthers(Request request) throws IOException {
		synchronized (this) {
			for (ClientThread clientsRunning : connected) {
				if (clientsRunning != this) {
					clientsRunning.processor.sendRequest(request);
				}
			}
		}
	}

}
