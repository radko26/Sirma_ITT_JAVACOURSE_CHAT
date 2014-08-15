package com.sirma.itt.javacourse.chatapp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Panel that holds components used for reading and sending instant messages.
 * 
 * @author Radoslav
 */
public class ChatPanel extends Panel {

	private static final long serialVersionUID = 965044186203967939L;
	private JTextField inputMsgField = new JTextField();
	private JTextArea oldMsgField = new JTextArea();
	private JScrollPane oldMsgFieldScroll = new JScrollPane(oldMsgField,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	private JTextArea onlineUsersField= new JTextArea();
	private JScrollPane onlineUsersFieldScroll = new JScrollPane(onlineUsersField,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	private JButton closeSessionButton = new JButton();

	/**
	 * Initialises all components and adds them to the panel.
	 * 
	 * @param frame
	 */
	public ChatPanel(JFrame frame) {
		super(frame);
		updateGUI();

		setPreferredSize(new Dimension(500, 360));
		setLayout(new FlowLayout());

		oldMsgFieldScroll.setPreferredSize(new Dimension(350, 300));
		oldMsgField.setEditable(false);
		inputMsgField.setPreferredSize(new Dimension(300, 25));
		onlineUsersFieldScroll.setPreferredSize(new Dimension(80,300));
		onlineUsersField.setEditable(false);
		
		add(oldMsgFieldScroll);
		add(onlineUsersFieldScroll);
		add(inputMsgField);
		add(closeSessionButton);
		
		

	}

	@Override
	public void updateGUI() {
		closeSessionButton.setText(ContentLanguageManager
				.getContent("close_session_button"));
	}

}
