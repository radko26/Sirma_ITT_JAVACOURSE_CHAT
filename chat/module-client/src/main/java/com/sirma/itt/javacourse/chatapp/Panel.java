package com.sirma.itt.javacourse.chatapp;

import java.awt.GraphicsEnvironment;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 *TODO
 * 
 * @author Radoslav
 */
public abstract class Panel extends JPanel {

	private static final long serialVersionUID = -6217509860743225606L;
	protected static final Point POINT_CENTER = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getCenterPoint();
	protected JFrame applicationFrameWindow;
	
	protected Panel(JFrame frame){
		applicationFrameWindow = frame;
	}
	
	/**
	 * Updates the components' title to the chosen language.
	 */
	abstract void updateGUI();
	/**
	 * Getter for the panel.
	 * @return
	 * The panel.
	 */
	protected JPanel getPanel(){
		return this;
	}
	
}
