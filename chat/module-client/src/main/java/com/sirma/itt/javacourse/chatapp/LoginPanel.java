package com.sirma.itt.javacourse.chatapp;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.ErrorManager;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Defines a {@link JPanel} that is used to hold all components used by the
 * login screen.
 * 
 * @author Radoslav
 */
public class LoginPanel extends Panel implements ActionListener {
	private static final long serialVersionUID = -8753238185409094676L;
	
	private JTextField usernameField = new JTextField();
	private JLabel usernameLabel = new JLabel();
	private JButton signUpButton = new JButton();
	private JLabel errorField = new JLabel();
	private boolean errorFromHost = false;

	/**
	 * Defines the panel and pass the frame reference.
	 * 
	 * @param frame
	 *            The main frame where it will be used.
	 */
	public LoginPanel(JFrame frame) {
		super(frame);
		width=350;
		height=65;
		updateGUI();
		setLayout(new FlowLayout());
		setPreferredSize(new Dimension(width, height));
		usernameField.setPreferredSize(new Dimension(185, 25));
		signUpButton.setMargin(new Insets(5, 5, 5, 5));
		signUpButton.addActionListener(this);
		
		errorField.setEnabled(true);
		errorField.setVisible(false);
		
		add(usernameLabel);
		add(usernameField);
		add(signUpButton);
		add(errorField);
		frame.setBounds(CENTER_POINT.x - width / 2, CENTER_POINT.y - height/ 2, width, height);
	}
	
	/**
	 * Sets error message on the screen.
	 */
	public void setErrorMsg(){
		errorField.setVisible(true);
		errorFromHost = false;
		errorField.setText(ContentLanguageManager.getContent("invalid_username_message"));
	}

	@Override
	public void updateGUI() {
		usernameLabel.setText(ContentLanguageManager.getContent("username_label"));
		signUpButton.setText(ContentLanguageManager.getContent("login_button"));
		if(errorField.isVisible() && !errorFromHost){
			errorField.setText(ContentLanguageManager.getContent("invalid_username_message"));
		}
		if(errorField.isVisible() && errorFromHost){
			errorField.setText(ContentLanguageManager.getContent("server_not_found"));
		}
	}
	
	
	
	/**
	 * Changes the screen to the next state, tries to connect to the server.
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			new Client(new AtomicBoolean(true),applicationFrameWindow).execute();
		} catch (IOException e1) {
			errorField.setVisible(true);
			errorField.setText(ContentLanguageManager.getContent("server_not_found"));
			errorFromHost = true;
		}
	}

}
