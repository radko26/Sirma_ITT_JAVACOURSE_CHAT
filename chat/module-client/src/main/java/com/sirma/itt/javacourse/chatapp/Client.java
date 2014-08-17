package com.sirma.itt.javacourse.chatapp;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
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
	private BlockingQueue<Request> toServer = new LinkedBlockingQueue<>();

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

		toServer.add(new Request().setContent("HOHAHAHHA").setType(
				Request.LOGIN_AUTH));
		ChatPanel chatPanel;
		while (running.get()) {
			if (!fromServer.isEmpty()) {
				Request request = fromServer.poll();
				if (request.getType() == Request.LOGIN_AUTH
						&& !request.isSuccessful()) {
					running.set(false);
					LoginPanel loginPanel = (LoginPanel) frame.getContentPane();
					loginPanel.setErrorMsg();
				} else {
					chatPanel = new ChatPanel(frame);
					frame.getContentPane().removeAll();
					frame.setContentPane(chatPanel);

					frame.getContentPane().repaint();
					frame.pack();
				}
			}
		}

		return null;
	}

	@Override
	protected void done() {
		System.out.println("Disconnecting from the server");
	}

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		Executor exe = new ScheduledThreadPoolExecutor(5);
		exe.execute(new Client(new AtomicBoolean(true)));
	}

}
