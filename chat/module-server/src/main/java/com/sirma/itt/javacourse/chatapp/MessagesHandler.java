package com.sirma.itt.javacourse.chatapp;

import java.util.Set;

/**
 * Handle all requests from the clients.
 * 
 * @author radoslav
 */
public class MessagesHandler {
	/**
	 * Checks whether the given username is valid.
	 * 
	 * @param loginAuth
	 *            The username.
	 * @return True if it is correct, otherwise false.
	 */
	public boolean checkUsernameAuth(Request loginAuth, Set<String> onlineUsers) {

		loginAuth.setContent(loginAuth.getContent().toLowerCase()
				.replaceAll("[\\s]+", ""));// formats in proper case and deletes
											// all spaces.
		String username = loginAuth.getContent();
		int type = loginAuth.getType();
		if (type != Request.LOGIN_AUTH) {
			return false;
		} else if (username.contains("[") || username.contains("]")
				|| username.contentEquals("")) {
			return false;
		} else if (!isUnique(username, onlineUsers)) {
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
	private synchronized boolean isUnique(String username,
			Set<String> onlineUsers) {
		return !onlineUsers.contains(username);
	}
	
	/**
	 * Modifies the content of the message request.
	 * 
	 * @param request
	 *            The request.
	 * @return The request with modified content.
	 */
	public Request handleChatMessages(Request request, String senderUsername) {
		StringBuilder newContent = new StringBuilder();
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
	
}
