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

	/**
	 * Passes this message to the {@link Logger} instance.
	 * 
	 * @param msg
	 *            The message.
	 */
	public static void log(String msg) {
		final String log = new StringBuilder(TIME_FORMAT.format(new Date()))
				.append(" ").append(msg).append("\n").toString();
		LogHandler.LOG.info(log);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				guiLoggingField.append(log);
			}
		});
	}

	/**
	 * Loads the {@link Logger} property file and initialises the GUI logField
	 * @param field
	 * The field.
	 */
	public static void setUpLogging(JTextArea field) {
		setUpLogging();
		guiLoggingField = field;
	}
	
	/**
	 * Loads the {@link Logger} property file.
	 */
	public static void setUpLogging(){
		PropertyConfigurator.configure(LogHandler.class
				.getResourceAsStream("log4j.properties"));
	}

}
