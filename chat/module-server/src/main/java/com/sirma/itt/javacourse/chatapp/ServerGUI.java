package com.sirma.itt.javacourse.chatapp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Creates the graphical user interface.
 * 
 * @author Radoslav
 */
public class ServerGUI extends JFrame implements ActionListener, MouseListener {
	/**
	 * Comment for serialVersionUID.
	 */
	private static final long serialVersionUID = -6375074313335408117L;

	private static final Point POINT_CENTER = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getCenterPoint();
	private JPanel contentPanel = new JPanel();
	private JTextArea logField = new JTextArea();
	private JButton stopServerButton = new JButton();
	private JScrollPane scroll = new JScrollPane(logField,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	private JPopupMenu languagePopUpMenu = new JPopupMenu();
	private JMenuItem languageOption = new JMenuItem(
			ContentLanguageManager.getContent("language_option"));
	
	private Server server;

	/**
	 * Initialises all containers in the frame.
	 */
	public ServerGUI() {
		updateGUI();
		stopServerButton.addActionListener(this);
		logField.setMargin(new Insets(5, 5, 2, 0));
		logField.setEnabled(false);
		scroll.setPreferredSize(new Dimension(300, 250));
		contentPanel.setLayout(new BorderLayout());

		contentPanel.add(scroll, BorderLayout.PAGE_START);
		contentPanel.add(stopServerButton, BorderLayout.PAGE_END);
		contentPanel.setPreferredSize(new Dimension(400, 300));
		contentPanel.setVisible(true);

		languagePopUpMenu.add(languageOption);
		languageOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ContentLanguageManager.setLanguage(ContentLanguageManager.getNextLanguage());
				updateGUI();
				
			}
		});
		logField.addMouseListener(this);
		addMouseListener(this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(contentPanel);
		pack();
		setBounds(POINT_CENTER.x - getWidth() / 2, POINT_CENTER.y - getHeight()
				/ 2, getWidth(), getHeight());
		setVisible(true);
		
		startServer();
		LogHandler.setUpLogging(logField);
		
	}
	
	private void startServer(){
		try {
			server= new Server();
			server.execute();
		} catch (IOException e) {
			System.out.println("Eroro");
		}
	}

	/**
	 * Changes the text's language of some containers using
	 * {@link ContentLanguageManager}.
	 */
	private void updateGUI() {
		setTitle(ContentLanguageManager.getContent("frame_title"));
		stopServerButton.setText(ContentLanguageManager
				.getContent("stop_server_button"));
		languageOption.setText(ContentLanguageManager.getContent("language_option")
				.concat(ContentLanguageManager.getNextLanguage()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			server.stop();
		} catch (IOException e1) {
			logField.append(e1.getMessage());
		}
		System.out.println("Stopping server");

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}
 
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			languagePopUpMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Main method to start it.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new ServerGUI();
	}

}
