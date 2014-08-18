package com.sirma.itt.javacourse.chatapp;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sirma.itt.javacourse.chatapp.Request;
import com.sirma.itt.javacourse.chatapp.RequestProcessor;

/**
 * Class that cope with each client.
 * 
 * @author Radoslav
 */
public class ClientHandleThread extends Thread {
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
			"hh:mm:ss");
	private static Set<String> onlineUsers = Collections
			.synchronizedSet(new HashSet<String>());
	private Socket client;
	private AtomicBoolean running;
	private List<ClientHandleThread> connected;
	private RequestProcessor processor = null;

	/**
	 * Initialises the socket,status of the server and list of connected.
	 * 
	 * @param client
	 *            The client socket.
	 * @param running
	 *            The status of the server.
	 * @param connected
	 *            List of all other connected.
	 */
	public ClientHandleThread(Socket client, AtomicBoolean running,
			List<ClientHandleThread> connected) {
		super(ClientHandleThread.class.getName());
		this.client = client;
		this.running = running;
		this.connected = connected;
	}

	@Override
	public void run() {
		Request userAuth = null;
		// userAuth holds the username as content if it connected to the server.
		try {
			processor = new RequestProcessor(client.getInputStream(),
					client.getOutputStream());
			userAuth = processor.receiveRequest();
			// Logs the try-connect msg.

			LogHandler.log(new StringBuilder().append("<")
					.append(userAuth.getContent()).append(">")
					.append("is trying to connect.").toString());
			if (checkUsernameAuth(userAuth)) {

				String msg = new StringBuilder().append("[")
						.append(TIME_FORMAT.format(new Date())).append("] ")
						.append("<").append(userAuth.getContent()).append("> ")
						.append("has connected.").toString();
				broadcastToOthers(new Request().setType(Request.CONNECTION)
						.setContent(msg));
				LogHandler.log(msg);// connected msg

				LogHandler.log("Client added");// added to the list of clients.
				connected.add(this);
				onlineUsers.add(userAuth.getContent());
				processor.sendRequest(new Request().setType(Request.LOGIN_AUTH)
						.setSuccessful(true)
						.setContent("Welcome " + userAuth.getContent() + "!")
						.addCollection(onlineUsers));

			} else {

				LogHandler.log(new StringBuilder("<")
						.append(userAuth.getContent()).append("> ")
						.append("username validation failed").toString());
				processor.sendRequest(new Request().setType(Request.LOGIN_AUTH)
						.setSuccessful(false).setContent("Invalid name"));
				processor.closeStream();
				client.close();

			}
			// Receiving requests.
			while (running.get()) {

				Request msg = handleChatMessages(processor.receiveRequest(),
						userAuth.getContent());
				processor.sendRequest(msg);
				broadcastToOthers(msg);

				LogHandler.log("broadcasting to all " + msg.getContent());

			}
		} catch (IOException | ClassNotFoundException e) {
			if (e instanceof IOException) {
				// User has disconnected.
				if (userAuth != null && !client.isClosed()) {
					Request disconnect = new Request().setType(
							Request.CONNECTION).setContent(
							new StringBuilder().append("[")
									.append(TIME_FORMAT.format(new Date()))
									.append("] ").append("<")
									.append(userAuth.getContent()).append("> ")
									.append("has disconnected.").toString());
					synchronized (connected) {
						connected.remove(this);
					}
					synchronized (onlineUsers) {
						onlineUsers.remove(userAuth.getContent());
					}
					try {
						client.close();// tries to close the socket.
					} catch (IOException e2) {
						LogHandler
								.log("error closing disconnected client socket");
					}
					try {
						broadcastToOthers(disconnect);
					} catch (IOException e1) {
						LogHandler
								.log("error in sending it to the other connected");
					}
					LogHandler.log(disconnect.getContent() + " msg sent!");
				}

			} else {
				LogHandler.log(e.toString());
			}
		} finally {
			if (processor != null) {
				processor.closeStream();
			}
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Modifies the content of the message request.
	 * 
	 * @param request
	 *            The request.
	 * @return The request with modified content.
	 */
	private Request handleChatMessages(Request request, String senderUsername) {
		StringBuilder newContent = new StringBuilder();
		newContent.append("[").append(TIME_FORMAT.format(new Date()))
				.append("] ");
		newContent.append("<").append(senderUsername).append("> ");
		StringBuffer message = new StringBuffer(request.getContent().trim());
		char firstLetter = message.charAt(0);
		if (firstLetter >= 'a' && firstLetter <= 'z') {
			firstLetter = (char) (firstLetter - 'a' + 'A');
		}
		message.setCharAt(0, firstLetter);
		newContent.append(message.toString());
		request.setContent(newContent.toString());
		return request;
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
		} else if (username.contains("[") || username.contains("]")
				|| username.contentEquals("")) {
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
	private synchronized boolean isUnique(String username) {
		return !onlineUsers.contains(username);
	}

	/**
	 * Sends the request to the list of connected clients.
	 * 
	 * @param request
	 *            The message which is wrapped by the {@link Request}.
	 * @throws IOException
	 *             In case of {@link IOException}.
	 */
	private void broadcastToOthers(Request request) throws IOException {
		synchronized (this) {
			for (ClientHandleThread clientsRunning : connected) {
				if (clientsRunning != this) {
					clientsRunning.processor.sendRequest(request);
				}
			}
		}
	}

}
