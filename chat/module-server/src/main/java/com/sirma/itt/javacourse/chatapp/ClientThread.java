package com.sirma.itt.javacourse.chatapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientThread extends Thread {

	private Socket client;
	private AtomicBoolean running;

	public ClientThread(Socket client, AtomicBoolean running) {
		this.client = client;
		this.running=running;
	}

	@Override
	public void run() {

		try {
			BufferedReader inputFromClient = new BufferedReader(
					new InputStreamReader(client.getInputStream()));
			PrintWriter outputToClient = new PrintWriter(
					client.getOutputStream(), true);
			System.out.println("ima me");
			outputToClient.println("Eto me");
		} catch (IOException e) {
			System.out.println("error s io v clientThread-a");// TODO
		}
		while (running.get()) {
			//System.out.println(running.get());
		}
		System.out.println("spiram da slusham");
	}

}
