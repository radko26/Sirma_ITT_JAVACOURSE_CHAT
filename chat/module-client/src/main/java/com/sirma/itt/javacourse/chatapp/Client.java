package com.sirma.itt.javacourse.chatapp;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.sirma.itt.javacourse.chattapp.Request;

/**
 * 
 * 
 * @author Radoslav
 */
public class Client extends SwingWorker<Void, Void> {

	private static final int PORT = 7001;
	private static final String HOST = "localhost";
	private Socket server;
	private AtomicBoolean running;
	private JFrame frame;
	private BlockingQueue<Request> fromServer = new LinkedBlockingQueue<>();
	private static BlockingQueue<Request> toServer = new LinkedBlockingQueue<>();

	/**
	 * Initialises the client login without using GUI.
	 * 
	 * @param running
	 * 
	 *            The state of the client.
	 * @throws UnknownHostException
	 *             If the host wasn't found.
	 * @throws IOException
	 *             In case of {@link IOException} regarded with {@link Socket}.
	 */
	public Client(AtomicBoolean running) throws UnknownHostException,
			IOException {
		server = new Socket(HOST, PORT);
		this.running = running;
	}

	/**
	 * Initialises the client logic with using {@link JFrame}.
	 * 
	 * @param running
	 *            The state of the client.
	 * @param frame
	 *            The GUI frame.
	 * @throws UnknownHostException
	 *             If the host wasn't found.
	 * @throws IOException
	 *             In case of {@link IOException} regarded with {@link Socket}.
	 * 
	 */
	public Client(AtomicBoolean running, JFrame frame)
			throws UnknownHostException, IOException {
		server = new Socket(HOST, PORT);
		this.running = running;
		this.frame = frame;
	}

	@Override
	protected Void doInBackground() {
		new ClientReader(running, server, fromServer).start();
		new ClientWriter(running, server, toServer).start();
		if (frame != null) {
			LoginPanel loginPanel = (LoginPanel) frame.getContentPane();
			toServer.add(new Request().setContent(
					loginPanel.getEnteredUsername())
					.setType(Request.LOGIN_AUTH));
		} else {
			toServer.add(new Request().setContent("radko").setType(
					Request.LOGIN_AUTH));
		}

		ChatPanel chatPanel = null;
		System.out.println(running.hashCode());
		while (running.get()) {
			if (!fromServer.isEmpty()) {
				Request request = fromServer.poll();
				if (request.getType() == Request.LOGIN_AUTH
						&& !request.isSuccessful()) {
					running.set(false);
					LoginPanel loginPanel = (LoginPanel) frame.getContentPane();
					loginPanel.setErrorMsg();
				} else if (request.getType() == Request.LOGIN_AUTH) {
					try {// to change to GUI panel.
						chatPanel = new ChatPanel(frame);
						frame.getContentPane().removeAll();
						frame.setContentPane(chatPanel);
						frame.getContentPane().repaint();
						frame.pack();

						chatPanel.log(request.getContent());
						LogHandler.log(request.getContent());
					} catch (NullPointerException e) {// without GUI.
						LogHandler.log(request.getContent());
					}
				} else if (request.getType() == Request.MESSAGE) {
					try {
						LogHandler.log(request.getContent());
						chatPanel.log(request.getContent());
					} catch (NullPointerException e) {
						LogHandler.log(request.getContent());
					}
				} else {
					// ONLINE USERS REQUEST
				}
			}
		}

		return null;
	}

	@Override
	protected void done() {
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (frame != null) {

			JOptionPane errorPane = new JOptionPane("Client disconnected.");
			JDialog errorPaneDialog = errorPane.createDialog(frame,"Disconnected");
			errorPaneDialog.setVisible(true);
		}

		LogHandler.log("disconnecting");
		System.exit(0);
	}

	/**
	 * 
	 * @param request
	 */
	public static void addMessageRequest(String content) {
		toServer.add(new Request().setType(Request.MESSAGE).setContent(content));
	}

	/**
	 * Stops the client.
	 */
	public void stopClientRunning() {
		running.set(false);
	}

	/**
	 * Main method to start it without GUI.
	 * 
	 * @param args
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void main(String[] args) throws UnknownHostException,
			IOException {
		Executor exe = new ScheduledThreadPoolExecutor(5);
		exe.execute(new Client(new AtomicBoolean(true)));
	}

}
