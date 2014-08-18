package com.sirma.itt.javacourse.chatapp;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Uses {@link Logger} to save all specified actions in a .log file.
 * 
 * @author Radoslav
 */
public class LogHandler {
	private static final Logger LOG = Logger.getLogger(Client.class);
	private static LogHandler handler;

	static {
		handler = new LogHandler();
	}

	/**
	 * Initialies the property file for the {@link Logger}.
	 */
	private LogHandler() {
		PropertyConfigurator.configure(LogHandler.class
				.getResourceAsStream("log4j.properties"));
	}

	/**
	 * Passes the parameter to the {@link Logger}.
	 * 
	 * @param msg
	 *            The message.
	 */
	public static void log(String msg) {
		LOG.info(msg);
	}

}
