package com.sirma.itt.javacourse.chatapp;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.sirma.itt.javacourse.chattapp.Request;

/**
 * Class that connects to the server on the given HOST/PORT.It starts two
 * threads for listening and writing to the server.If GUI is presented it writes
 * data to it.
 * 
 * @author Radoslav
 */
public class Client extends SwingWorker<Void, Void> {

	private static final int PORT = 7001;
	private static final String HOST = "localhost";
	private static BlockingQueue<Request> toServer = new LinkedBlockingQueue<>();
	private BlockingQueue<Request> fromServer = new LinkedBlockingQueue<>();
	private Set<String> onlineUsers;
	private static final Pattern PATTERN = Pattern.compile("<([^<|>]+?)>");
	private Socket server;
	private AtomicBoolean running;
	private JFrame frame;

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
						onlineUsers = new HashSet(request.getCollection());
						LogHandler.log(request.getContent());
						try {
							updateOnlineUsersGUI(chatPanel);
						} catch (InvocationTargetException
								| InterruptedException e) {
							e.printStackTrace();
						}
					} catch (NullPointerException e) {// without GUI.
						LogHandler.log(request.getContent());
					}
				} else if (request.getType() == Request.MESSAGE) {
					try {
						chatPanel.log(request.getContent());
					} catch (NullPointerException e) {
						LogHandler.log(request.getContent());
					}
				} else {// Request for disconnected or connected user.
					System.out.println(request.getContent());

					Matcher matcher = PATTERN.matcher(request.getContent());
					matcher.find();
					String username = matcher.group(1);
					if (onlineUsers.contains(username)) {
						onlineUsers.remove(username);
						if (frame != null) {
							try {
								updateOnlineUsersGUI(chatPanel);
							} catch (InvocationTargetException
									| InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else {
						onlineUsers.add(username);
						try {
							updateOnlineUsersGUI(chatPanel);
						} catch (InvocationTargetException
								| InterruptedException e) {
							e.printStackTrace();
						}
					}
					chatPanel.log(request.getContent());
				}
			}
		}

		return null;
	}

	/**
	 * Loops through the connected users and draw them to the GUI field.
	 * 
	 * @throws InterruptedException
	 *             If the process has been interrupted.
	 * @throws InvocationTargetException
	 */
	private void updateOnlineUsersGUI(final ChatPanel panel)
			throws InvocationTargetException, InterruptedException {
		final StringBuilder list = new StringBuilder();
		for (String user : onlineUsers) {
			list.append(user).append("\n");
		}
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				panel.getOnlineUsersField().setText(list.toString());
			}
		});
		System.out.println(list.toString());

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
			JDialog errorPaneDialog = errorPane.createDialog(frame,
					"Disconnected");
			errorPaneDialog.setVisible(true);
		}

		LogHandler.log("disconnecting");
		System.exit(0);
	}

	/**
	 * Creates request
	 * 
	 * @param content
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
