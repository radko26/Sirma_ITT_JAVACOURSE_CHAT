package com.sirma.itt.javacourse.chatapp;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

	/**
	 * Defines the panel and pass the frame reference.
	 * 
	 * @param frame
	 *            The main frame where it will be used.
	 */
	public LoginPanel(JFrame frame) {
		super(frame);
		updateGUI();
		setLayout(new FlowLayout());
		setPreferredSize(new Dimension(350, 65));
		usernameField.setPreferredSize(new Dimension(185, 25));
		signUpButton.setMargin(new Insets(5, 5, 5, 5));
		signUpButton.addActionListener(this);
		add(usernameLabel);
		add(usernameField);
		add(signUpButton);

	}

	@Override
	public void updateGUI() {
		usernameLabel.setText(ContentLanguageManager.getContent("username_label"));
		signUpButton.setText(ContentLanguageManager.getContent("login_button"));
	}

	/**
	 * Changes the screen to the next state.
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("clicked");
		applicationFrameWindow.validate();
		applicationFrameWindow.getContentPane().removeAll();

		JPanel panel = new JPanel();
		panel.add(new JLabel("Label"));
		panel.setPreferredSize(new Dimension(500, 500));
		panel.setVisible(true);
		applicationFrameWindow.setContentPane(new ChatPanel(applicationFrameWindow).getPanel());
		applicationFrameWindow.pack();
		applicationFrameWindow.setBounds(POINT_CENTER.x
				- applicationFrameWindow.getWidth() / 2, POINT_CENTER.y
				- applicationFrameWindow.getHeight() / 2,
				applicationFrameWindow.getWidth(),
				applicationFrameWindow.getHeight());
	}

}
