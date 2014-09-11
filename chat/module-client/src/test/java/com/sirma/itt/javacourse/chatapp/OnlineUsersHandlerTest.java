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

	/**
	 * Initialises objects.
	 */
	@Before
	public void init() {
		Set<String> testSet = new HashSet<>();
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

}
