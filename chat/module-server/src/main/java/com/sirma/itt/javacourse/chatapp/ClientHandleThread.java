package com.sirma.itt.javacourse.chatapp;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sirma.itt.javacourse.chattapp.Request;
import com.sirma.itt.javacourse.chattapp.RequestProcessor;

/**
 * Class that cope with each client.
 * 
 * @author Radoslav
 */
public class ClientHandleThread extends Thread {
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
		try {
			processor = new RequestProcessor(client.getInputStream(),
					client.getOutputStream());
			userAuth = processor.receiveRequest();
			LogHandler.log(new StringBuilder("<").append(userAuth.getContent())
					.append(">").append("is trying to connect.").toString());// trying
																				// to
																				// connect
																				// msg logged
			if (checkUsernameAuth(userAuth)) {
				String msg = new StringBuilder("<")
						.append(userAuth.getContent()).append("> ")
						.append("has connected.").toString();
				broadcastToOthers(new Request().setType(Request.MESSAGE)
						.setContent(msg));
				LogHandler.log(msg);// connected msg

				LogHandler.log("Client added");// added to the list of clients.
				connected.add(this);
				processor.sendRequest(new Request().setType(Request.LOGIN_AUTH)
						.setSuccessful(true)
						.setContent("Welcome " + userAuth.getContent() + "!"));
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
				Request msg = processor.receiveRequest();
				processor.sendRequest(msg);

				broadcastToOthers(handleChatMessages(msg, userAuth.getContent()));
				LogHandler.log("broadcasting to all " + msg.getContent());
			}
		} catch (IOException | ClassNotFoundException e) {
			if (e instanceof IOException) {
				if (userAuth != null && !client.isClosed()) {
					Request disconnect = new Request().setType(
							Request.CONNECTION).setContent(
							new StringBuilder("<")
									.append(userAuth.getContent()).append("> ")
									.append("has disconnected.").toString());// constructing
																				// disconnected
																				// msg
					connected.remove(this);
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
		}
	}

	/**
	 * Modifies the content of the message request.
	 * @param request
	 * The request.
	 * @return
	 * The request with modified content.
	 */
	private Request handleChatMessages(Request request, String senderUsername){
		StringBuilder newContent = new StringBuilder();
		newContent.append("<").append(senderUsername).append(">");
		StringBuffer message = new StringBuffer(request.getContent().trim());
		char firstLetter  = message.charAt(0);
		if(firstLetter >= 'a' && firstLetter <='z'){
			firstLetter = (char) (firstLetter - 'a' + 'A');
		}
		message.setCharAt(0, firstLetter);
		newContent.append(" ").append(message.toString());
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
	private boolean isUnique(String username) {
		return true;
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
