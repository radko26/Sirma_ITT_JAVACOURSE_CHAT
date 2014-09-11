package com.sirma.itt.javacourse.chatapp;

import java.util.Set;

/**
 * Wraps up {@link Set}.
 * 
 * @author radoslav
 *
 */
public class OnlineUsersHandler {

	private Set<String> onlineUsers;

	/**
	 * Initialises the set.
	 * 
	 * @param onlineUsers
	 */
	public OnlineUsersHandler(Set<String> onlineUsers) {
		this.onlineUsers = onlineUsers;
	}

	/**
	 * Checks if the users is contained in the set.
	 * 
	 * @param name
	 *            The name of the user.
	 * @return True if it member of the set and false otherwise.
	 */
	public boolean contains(String name) {
		return onlineUsers.contains(name);
	}

	/**
	 * Removes name from the set.
	 * 
	 * @param name
	 *            The name of the user.
	 * @return True if it is successfully removed.
	 */
	public boolean remove(String name) {
		return onlineUsers.remove(name);
	}

	/**
	 * Adds name into the set.
	 * 
	 * @param name
	 *            The name of the user.
	 * @return True if it is successfully added.
	 */
	public boolean add(String name) {
		return onlineUsers.add(name);
	}

	/**
	 * Gets the set.
	 */
	public Set<String> getUsers() {
		return onlineUsers;
	}
}
