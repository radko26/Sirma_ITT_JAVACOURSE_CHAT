package com.sirma.itt.javacourse.chatapp;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Uses {@link Logger} to save all specified actions in a .log file.
 * 
 * @author Radoslav
 */
public class LogHandler {
	private static final Logger LOG = Logger.getLogger(Client.class);
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
			"hh:mm:ss");
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

	/**
	 * Gets the current time.
	 * 
	 * @return String representing the time.
	 */
	public static String getTime() {
		return new StringBuilder().append("[")
				.append(TIME_FORMAT.format(new Date())).append("] ").toString();
	}

}
