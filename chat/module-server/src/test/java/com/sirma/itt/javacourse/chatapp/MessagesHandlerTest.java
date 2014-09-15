package com.sirma.itt.javacourse.chatapp;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link MessagesHandlerTest}.
 * 
 * @author radoslav
 *
 */
public class MessagesHandlerTest {

	private MessagesHandler messagesHandler;
	private Set<String> onlineUsers;

	/**
	 * Initialises the handler.
	 */
	@Before
	public void init() {
		messagesHandler = new MessagesHandler();
		onlineUsers = new HashSet<String>();
		onlineUsers.add("ed");
		onlineUsers.add("eddy");
	}

	/**
	 * Checks a allowed name.
	 */
	@Test
	public void testUserAuthWithAllowedName() {
		Request mockRequest = new Request().setType(Request.LOGIN_AUTH)
				.setContent("santo");
		assertTrue(messagesHandler.checkUsernameAuth(mockRequest, onlineUsers));
	}

	/**
	 * Checks a name which is already included into the chat room.
	 */
	@Test
	public void testUserAuthWithNotUniqueUsername() {
		Request mockRequest = new Request().setType(Request.LOGIN_AUTH)
				.setContent("ed");
		assertTrue(!messagesHandler.checkUsernameAuth(mockRequest, onlineUsers));
	}

	/**
	 * Checks names with forbidden symbols.
	 */
	@Test
	public void testUserAuthWithForbiddenName() {
		Request mockRequest = new Request().setType(Request.LOGIN_AUTH)
				.setContent("ricko[");
		assertTrue(!messagesHandler.checkUsernameAuth(mockRequest, onlineUsers));
		mockRequest.setContent("ricko]");
		assertTrue(!messagesHandler.checkUsernameAuth(mockRequest, onlineUsers));
		mockRequest.setContent("");
		assertTrue(!messagesHandler.checkUsernameAuth(mockRequest, onlineUsers));

	}

	/**
	 * Checks a request which is not for authentication.
	 */
	@Test
	public void testUserAuthWithWrongRequest() {
		Request mockRequest = new Request().setType(Request.MESSAGE)
				.setContent("asa");
		assertTrue(!messagesHandler.checkUsernameAuth(mockRequest, onlineUsers));
	}

	/**
	 * Checks if it makes
	 */
	@Test
	public void testHandleChatMessages() {
		String mockUsername = "radko";
		StringBuffer mockMessageCont = new StringBuffer("uni");
		Request mockMessage = new Request().setContent(mockMessageCont
				.toString());

		String methodOutput = messagesHandler.handleChatMessages(mockMessage,
				mockUsername).getContent();
		mockMessageCont.setCharAt(0, toUpperCase(mockMessageCont.charAt(0)));
		assertEquals(new StringBuilder().append("<").append(mockUsername)
				.append(">").append(" ").append(mockMessageCont).toString(),
				methodOutput);
	}

	/**
	 * Changes lowercase char to capital.
	 * 
	 * @param charAt
	 *            The char
	 * @return The uppercase char.
	 */
	private char toUpperCase(char letter) {
		return (letter >= 'a' && letter <= 'z') ? (char) (letter - 'a' + 'A')
				: letter;
	}

}
