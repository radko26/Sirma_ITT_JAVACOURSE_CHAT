package com.sirma.itt.javacourse.chattapp;

import java.io.Serializable;

/**
 * Wrapper class that initialises object used for communication between the
 * client/server application.
 * 
 * @author Radoslav
 */
public class Request implements Serializable {

	private static final long serialVersionUID = -3234091786911332542L;
	public static final int LOGIN_AUTH = 1;
	public static final int MESSAGE = 2;
	public static final int CONNECTION = 3;
	

	private int type;
	private boolean isSuccesful;

	private String content;

	/**
	 * Sets the type of the request.
	 * 
	 * @param type
	 *            The type of the request.
	 * @return The same instance of the class but with changed type field.
	 */
	public Request setType(int type) {
		this.type = type;
		return this;
	}

	/**
	 * Sets the content.
	 * 
	 * @param content
	 *            The content.
	 * @return Same instance of this class.
	 */
	public Request setContent(String content) {
		this.content = content;
		return this;
	}

	/**
	 * Set true if the login attempt has been successful and false otherwise.
	 * 
	 * @return Same instance of this class.
	 */
	public Request setSuccessful(boolean status) {
		this.isSuccesful = status;
		return this;
	}

	/**
	 * Getter for this type.
	 * 
	 * @return The type of the request.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Getter for this content.
	 * 
	 * @return The content of the request.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Getter for the login authentication state.
	 * 
	 * @return The state - true if it is successful and false the other way
	 *         round.
	 */
	public boolean isSuccessful() {
		return isSuccesful;
	}

}
