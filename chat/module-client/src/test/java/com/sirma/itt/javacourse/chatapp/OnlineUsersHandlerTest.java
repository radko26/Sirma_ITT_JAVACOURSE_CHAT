package com.sirma.itt.javacourse.chatapp;

import static org.junit.Assert.*;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the {@link OnlineUsersHandler}
 * 
 * @author radoslav
 */
public class OnlineUsersHandlerTest {

	private OnlineUsersHandler users;
	private Set<String> testSet = new HashSet<>();

	/**
	 * Initialises objects.
	 */
	@Before
	public void init() {
		
		testSet.add("Masha");
		testSet.add("Medved");

		users = new OnlineUsersHandler(testSet);
	}

	/**
	 * Tries to add new query into the set. If it passes then the query is added
	 * successfully.
	 */
	@Test
	public void testAddNew() {
		assertTrue(users.add("Santo"));
	}

	/**
	 * Tries to add existing query into the set. If it passes then the query is
	 * not added.
	 */
	@Test
	public void testAddExisting() {
		assertTrue(!users.add("Masha"));
	}

	/**
	 * Removes query.
	 */
	@Test
	public void testRemoveExisting() {
		assertTrue(users.remove("Medved"));

	}

	/**
	 * Tries to remove not existing query, it must return false.
	 */
	@Test
	public void testRemoveNotExisting() {
		assertTrue(!users.remove("Lady"));

	}

	/**
	 * Checks if a query is contained into the set.
	 */
	@Test
	public void testContains() {
		assertTrue(users.contains("Medved"));
		assertTrue(!users.contains("Lady In Black"));
	}
	
	/**
	 * Checks returned list full of online users.
	 */
	@Test
	public void testGetUsers(){
		assertEquals(testSet.toString(),users.getUsers().toString());
	}

}
