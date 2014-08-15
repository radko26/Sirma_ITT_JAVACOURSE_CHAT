package com.sirma.itt.javacourse.chatapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.swing.SwingWorker;

/**
 * 
 * 
 * @author Radoslav
 */
public class Client extends SwingWorker<Void, Void> {

	private static final int PORT = 7001;
	private static final String HOST = "localhost";
	private Socket server;

	public Client() throws UnknownHostException, IOException {
		server = new Socket(HOST, PORT);
	}

	@Override
	protected Void doInBackground() throws Exception {

		BufferedReader inputFromServer = new BufferedReader(
				new InputStreamReader(server.getInputStream()));

		while (true) {
			if(server.isClosed()){
				System.out.println("SERVER CLOSED DISCONNECTING");
			}
			if (inputFromServer.ready()) {
				System.out.println(inputFromServer.readLine());
			}

		}
	}

	@Override
	protected void done() {
		System.out.println("I am done with all the things");
	}
	
	private void checkIfServerIsAvailable(){
		try {
			Socket checked = new Socket(HOST,PORT);
		} catch (IOException e) {
			//setAvailable(false); 
		}
		
	}

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		Executor exe = new ScheduledThreadPoolExecutor(5);
		exe.execute(new Client());
	}

}
