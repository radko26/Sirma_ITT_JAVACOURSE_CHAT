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

	static {
		PropertyConfigurator.configure(LogHandler.class
				.getResourceAsStream("log4j.properties"));
	}

	/**
	 * Disable further instances.
	 */
	private LogHandler() {
		
	}

	/**
	 * Passes the parameter to the {@link Logger}.
	 * 
	 * @param msg
	 *            The message.
	 */
	public static void log(String msg) {
		LogHandler.LOG.info(msg);
	}

}
