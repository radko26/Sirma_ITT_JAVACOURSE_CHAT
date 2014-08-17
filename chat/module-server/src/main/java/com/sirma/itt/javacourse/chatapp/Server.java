package com.sirma.itt.javacourse.chatapp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author Radoslav
 */
public class Server extends SwingWorker<Void, Void>{
	private static final int PORT = 7001;
	private ServerSocket server;
	private Socket client;
	private AtomicBoolean running=new AtomicBoolean(true);
	private List<ClientThread> connected = Collections.synchronizedList(new ArrayList<ClientThread>());
	
	public Server() throws IOException{
		server=new ServerSocket(PORT);
	}
	
	
	public void stop() throws IOException{
		running.set(false);
		server.close();
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		
		while(running.get()){
			client = server.accept();
			new ClientThread(client, running,connected).start();
		}
		return null;
	}

	@Override
	protected void done() {
		System.out.println("Server is stopped");
	}
	
}
