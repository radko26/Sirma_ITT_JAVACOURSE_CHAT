package com.sirma.itt.javacourse.chatapp;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
public class ChatPanel extends Panel implements KeyListener {

	private static final long serialVersionUID = 965044186203967939L;
	private JTextField inputMsgField = new JTextField();
	private JTextArea oldMsgField = new JTextArea();
	private JScrollPane oldMsgFieldScroll = new JScrollPane(oldMsgField,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	private JTextArea onlineUsersField = new JTextArea();
	private JScrollPane onlineUsersFieldScroll = new JScrollPane(
			onlineUsersField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private JButton closeSessionButton = new JButton();
	private StringBuilder msg = new StringBuilder("");

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
		inputMsgField.addKeyListener(this);
		onlineUsersFieldScroll.setPreferredSize(new Dimension(80, 300));
		onlineUsersField.setEditable(false);
		closeSessionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				running.set(false);
				System.exit(0);
			}
		});
		add(oldMsgFieldScroll);
		add(onlineUsersFieldScroll);
		add(inputMsgField);
		add(closeSessionButton);
	}

	/**
	 * Adds message to the {@link JTextArea}.
	 * 
	 * @param msg
	 *            The message.
	 */
	public void log(String msg) {
		msg = LogHandler.getTime() + msg + "\n";
		oldMsgField.append(msg);
	}

	/**
	 * Getter for the field where online users are logged.
	 * @return
	 * The {@link JTextArea};
	 */
	public JTextArea getOnlineUsersField(){
		return onlineUsersField;
	}
	
	@Override
	public void updateGUI() {
		closeSessionButton.setText(ContentLanguageManager
				.getContent("close_session_button"));
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (msg.length() > 0 && msg.length() < 201) {
				Client.addMessageRequest(msg.toString());// send message.
				inputMsgField.setText("");
				msg.delete(0, msg.length());
			}
		} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			if (msg.length() > 0) {
				msg.deleteCharAt(msg.length() - 1);
			}
		} else if(e.getKeyCode()!= KeyEvent.VK_SHIFT && e.getKeyCode()!=KeyEvent.VK_CONTROL && e.getKeyCode()!=KeyEvent.VK_ALT){
			msg.append(e.getKeyChar());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

}
