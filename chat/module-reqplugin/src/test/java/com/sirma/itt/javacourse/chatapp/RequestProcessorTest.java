package com.sirma.itt.javacourse.chatapp;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Tests whether {@link Request} and {@link RequestProcessor} communicate
 * correctly.
 * 
 * @author Radoslav
 */
public class RequestProcessorTest {

	/**
	 * Creates a {@link RequestProcessor} and saves it to a file which is after
	 * used from the {@link ReqestProcessor}.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@Test
	public void testGetTypeAndGetContentAndGetCollection() throws ClassNotFoundException,
			IOException {
		List<Integer> testCollection  = new ArrayList<>();
		testCollection.add(5);
		Request request = new Request().setType(Request.MESSAGE).setContent("Conta").addCollection(testCollection);
		RequestProcessor processor = new RequestProcessor(new FileOutputStream(new File("save.ser")));
		processor.sendRequest(request);
		processor = new RequestProcessor(new FileInputStream(
				new File("save.ser")));
		Request recieved = processor.receiveRequest();
		
		assertEquals(Request.MESSAGE, recieved.getType());
		assertEquals("Conta", recieved.getContent());
		assertEquals(testCollection, recieved.getCollection());
		processor.closeStream();
	}

}
