package com.sirma.itt.javacourse.chatapp;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * Class that connects to the server on the given HOST/PORT.It starts two
 * threads for listening and writing to the server.If GUI is presented it writes
 * data to it.
 * 
 * @author Radoslav
 */
public class Client extends SwingWorker<Void, Void> {
	private static final Pattern PATTERN = Pattern.compile("<([^<|>]+?)>");
	private static int PORT;
	private static String HOST;
	private static BlockingQueue<Request> toServer = new LinkedBlockingQueue<>();
	private BlockingQueue<Request> fromServer = new LinkedBlockingQueue<>();
	private OnlineUsersHandler onlineUsers;
	private ClientReader clientReader;
	private ClientWriter clientWriter;
	private Socket server;
	private AtomicBoolean running;
	private AtomicBoolean isServerRunning = new AtomicBoolean(true);
	private boolean errorInUsername = false;
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
		setConnectionProperties();
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
		setConnectionProperties();
		server = new Socket(HOST, PORT);
		this.running = running;
		this.frame = frame;
	}

	/**
	 * Loads the property file and sets the host and the port.
	 * 
	 * @throws IOException
	 *             For any problems with the input stream.
	 */
	private void setConnectionProperties() throws IOException {
		Properties properties = new Properties();
		properties
				.load(this.getClass().getResourceAsStream("setup.properties"));
		HOST = properties.getProperty("host", "localhost");
		PORT = Integer.valueOf(properties.getProperty("port", "7001"));
	}

	@Override
	protected Void doInBackground() {
		clientReader = new ClientReader(running, isServerRunning, server,
				fromServer);
		clientWriter = new ClientWriter(running, server, toServer);
		clientReader.start();
		clientWriter.start();
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
		while (running.get() && isServerRunning.get()) {
			if (!fromServer.isEmpty()) {
				Request request = fromServer.poll();
				if (request.getType() == Request.LOGIN_AUTH
						&& !request.isSuccessful()) {
					running.set(false);
					if (frame != null) {
						LoginPanel loginPanel = (LoginPanel) frame
								.getContentPane();
						loginPanel.setErrorMsg(request.getContent());
					}
					errorInUsername = true;
				} else if (request.getType() == Request.LOGIN_AUTH) {
					onlineUsers = new OnlineUsersHandler(new HashSet<String>(
							(Collection<? extends String>) request
									.getCollection()));
					LogHandler.log(request.getContent());
					if (frame != null) {// to change to GUI panel.
						chatPanel = new ChatPanel(frame);
						frame.getContentPane().removeAll();
						frame.setContentPane(chatPanel);
						frame.getContentPane().repaint();
						frame.pack();
						chatPanel.log(request.getContent());
						updateOnlineUsersGUI(chatPanel);
					}
				} else if (request.getType() == Request.MESSAGE) {
					if (frame != null) {
						chatPanel.log(request.getContent());
					}
					LogHandler.log(request.getContent());
				} else {// Request for disconnected or connected user.
					Matcher matcher = PATTERN.matcher(request.getContent());
					matcher.find();
					String username = matcher.group(1);
					if (onlineUsers.contains(username)) {
						onlineUsers.remove(username);
						if (frame != null) {
							chatPanel.log(request.getContent());
							updateOnlineUsersGUI(chatPanel);
						} else {// no gui specified log it to the file and
								// console.
							LogHandler.log(username
									+ " removed from the list of online users");
						}
					} else {
						onlineUsers.add(username);
						if (frame != null) {// if there is no frame null pointer
							// will be thrown.
							chatPanel.log(request.getContent());
							updateOnlineUsersGUI(chatPanel);
						} else {
							LogHandler.log(username
									+ " added to the list of users");
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * Loops through the connected users and draw them to the GUI field.
	 */
	private void updateOnlineUsersGUI(final ChatPanel panel) {
		final StringBuilder list = new StringBuilder();
		for (String user : onlineUsers.getUsers()) {
			list.append(user).append("\n");
		}

		panel.getOnlineUsersField().setText(list.toString());

	}

	@Override
	protected void done() {
		if (isServerRunning.get() == false && !errorInUsername) {
			if (frame != null) {

				JOptionPane errorPane = new JOptionPane(
						"Server has stopped working.");
				JDialog errorPaneDialog = errorPane
						.createDialog("Critical error.");
				errorPaneDialog.setVisible(true);
			}
			LogHandler.log("Server stopped");
		}
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		new Thread(new Client(new AtomicBoolean(true))).start();
	}

}
