package com.sirma.itt.javacourse.chatapp;

import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * Creates graphical user interface to wrap the client framework.
 * 
 * @author Radoslav
 */
public class ClientGUI extends JFrame implements MouseListener {

	private static final long serialVersionUID = -5998670976677266097L;
	private JPopupMenu languagePopUpMenu = new JPopupMenu();
	private JMenuItem languageOption = new JMenuItem(ContentLanguageManager
			.getContent("language_option").concat(
					ContentLanguageManager.getNextLanguage()));

	/**
	 * Initialises the main frame window.
	 */
	public ClientGUI() {
		super(ContentLanguageManager.getContent("frame_title"));
		setContentPane(new LoginPanel(this));
		pack();
		addMouseListener(this);
		languageOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ContentLanguageManager.setLanguage(ContentLanguageManager
						.getNextLanguage());
				Panel panels = (Panel) getContentPane();
				panels.updateGUI();
				updateGUI();
			}
		});
		languagePopUpMenu.add(languageOption);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setVisible(true);
	}

	/**
	 * Main method to start the program.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new ClientGUI();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			languagePopUpMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Updates frame components with the new language.
	 */
	public void updateGUI() {
		setTitle(ContentLanguageManager.getContent("frame_title"));
		languageOption.setText(ContentLanguageManager.getContent(
				"language_option").concat(
				ContentLanguageManager.getNextLanguage()));
	}
}
