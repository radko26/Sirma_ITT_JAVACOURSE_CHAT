package com.sirma.itt.javacourse.chatapp;

import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * Abstract class that provides basic skeleton of all sub-panels.
 * 
 * @author Radoslav
 */
public abstract class Panel extends JPanel {

	private static final long serialVersionUID = -6217509860743225606L;
	protected static final Point CENTER_POINT = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getCenterPoint();
	protected static int width = 350;
	protected static int height = 65;
	protected JFrame applicationFrameWindow;
	protected static AtomicBoolean running = new AtomicBoolean(true);

	/**
	 * Initialises the running frame.
	 * 
	 * @param frame
	 *            The frame.
	 */
	protected Panel(JFrame frame) {
		applicationFrameWindow = frame;
	}

	/**
	 * Updates the components' title to the chosen language.
	 */
	public abstract void updateGUI();

	/**
	 * Getter for the panel.
	 * 
	 * @return The panel.
	 */
	protected JPanel getPanel() {
		return this;
	}

}
