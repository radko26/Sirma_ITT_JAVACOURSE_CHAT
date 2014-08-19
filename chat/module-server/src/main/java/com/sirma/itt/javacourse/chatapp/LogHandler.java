package com.sirma.itt.javacourse.chatapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Handles the logging using {@link Logger}.
 * 
 * @author Radoslav
 */
public class LogHandler {
	private static final Logger LOG = Logger.getLogger("Server");
	private static final DateFormat TIME_FORMAT = new SimpleDateFormat(
			"hh:mm:ss");
	private static JTextArea guiLoggingField;
	private static LogHandler handler;

	static {
		handler = new LogHandler();
	}

	/**
	 * Initialises the logger.
	 */
	private LogHandler() {
		setUpLogging();
	}

	/**
	 * Passes this message to the {@link Logger} instance.
	 * 
	 * @param msg
	 *            The message.
	 */
	public static void log(String msg) {
		final String log = new StringBuilder().append("[ ")
				.append(TIME_FORMAT.format(new Date())).append("] ")
				.append(msg).append("\n").toString();

		if (guiLoggingField != null) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					guiLoggingField.append(log);
				}
			});
		} else {
			handler.LOG.info(log);
		}
	}

	/**
	 * Loads the {@link Logger} property file and initialises the GUI logField
	 * 
	 * @param field
	 *            The field.
	 */
	public static void setUpLogging(JTextArea field) {
		setUpLogging();
		guiLoggingField = field;
	}

	/**
	 * Loads the {@link Logger} property file.
	 */
	private static void setUpLogging() {
		PropertyConfigurator.configure(LogHandler.class
				.getResourceAsStream("log4j.properties"));
	}

}
