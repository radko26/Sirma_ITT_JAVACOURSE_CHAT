package com.sirma.itt.javacourse.chatapp;

import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class that cope with each client.
 * 
 * @author Radoslav
 */
public class ClientHandleThread extends Thread {
	private static Set<String> onlineUsers = new HashSet<String>();
	private Socket client;
	private AtomicBoolean running;
	private List<ClientHandleThread> connected;
	private RequestProcessor processor = null;
	private MessagesHandler messagesHandler;

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
		// userAuth holds the username as content if it is connected to the
		// server.
		messagesHandler = new MessagesHandler();
		try {
			processor = new RequestProcessor(client.getInputStream(),
					client.getOutputStream());
			userAuth = processor.receiveRequest();
			// Logs the auth-connect msg.

			LogHandler.log(new StringBuilder()
					.append("<")
					.append(userAuth.getContent())
					.append(">")
					.append(ContentLanguageManager
							.getContent("user_trying_to_connect")).toString());
			if (messagesHandler.checkUsernameAuth(userAuth, onlineUsers)) {

				broadcastToOthers(new Request().setType(Request.CONNECTION)
						.setContent(userAuth.getContent()));
				String msg = new StringBuilder()
						.append("<")
						.append(userAuth.getContent())
						.append("> ")
						.append(ContentLanguageManager
								.getContent("user_has_connected")).toString();
				LogHandler.log(msg);// connected msg

				LogHandler.log(ContentLanguageManager.getContent("user_added"));
				
				connected.add(this);
				onlineUsers.add(userAuth.getContent());
				processor.sendRequest(new Request()
						.setType(Request.LOGIN_AUTH)
						.setSuccessful(true)
						.setContent(
								"<Server> Welcome " + userAuth.getContent()
										+ "!").addCollection(onlineUsers));

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

				Request msg = messagesHandler.handleChatMessages(
						processor.receiveRequest(), userAuth.getContent());
				processor.sendRequest(msg);
				broadcastToOthers(msg);

				LogHandler.log(ContentLanguageManager
						.getContent("broadcasting_to_all") + msg.getContent());

			}
		} catch (IOException | ClassNotFoundException e) {
			if (e instanceof IOException) {
				// User has disconnected.
				if (userAuth != null && !client.isClosed()) {
					Request disconnect = new Request().setType(
							Request.CONNECTION).setContent(
							userAuth.getContent());
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
					LogHandler.log(disconnect.getContent() + " "+ ContentLanguageManager.getContent("user_has_disconnected"));
					try {
						broadcastToOthers(disconnect);
					} catch (IOException e1) {
						LogHandler
								.log("error in sending it to the other connected");
					}
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
