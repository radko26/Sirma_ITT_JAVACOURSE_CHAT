package com.sirma.itt.javacourse.chatapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Processes requests and unwraps them.
 * 
 * @author Radoslav
 */
public class RequestProcessor {

	private InputStream inStream;
	private OutputStream outStream;

	/**
	 * Constructs {@link RequestProcessor} instance with inputStream as
	 * parameter used for deserializing {@link Request}.
	 * 
	 * @param stream
	 *            Instance of {@link InputStream}.
	 */
	public RequestProcessor(InputStream stream) {
		this.inStream = stream;
	}

	/**
	 * Constructs {@link RequestProcessor} instance with {@link OutputStream} as
	 * parameter used for serializing {@link Request}.
	 * 
	 * @param stream
	 *            Instance of {@link OutputStream}.
	 */
	public RequestProcessor(OutputStream stream) {
		this.outStream = stream;
	}

	/**
	 * Constructs {@link RequestProcessor} instance with both
	 * {@link InputStream and @link OutputStream}.
	 * 
	 * @param inStream
	 *            Instance of {@link InputStream}.
	 * @param outStream
	 *            Instance of {@link OutputStream.
	 */
	public RequestProcessor(InputStream inStream, OutputStream outStream) {
		this.inStream = inStream;
		this.outStream = outStream;
	}

	/**
	 * Deserializes objects from this {@link InputStream}.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Request receiveRequest() throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(inStream);
		return (Request) in.readObject();
	}

	/**
	 * Serializes requests.
	 * 
	 * @param request
	 *            The request.
	 * @throws IOException
	 *             In case of {@link IOException} occurs.
	 */
	public void sendRequest(Request request) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(outStream);
		out.writeObject(request);
	}

	/**
	 * Closes all open streams.
	 */
	public void closeStream() {
		if (inStream != null) {
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (outStream != null) {
			try {
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
