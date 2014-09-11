package com.sirma.itt.javacourse.chatapp;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Loads {@link ResourceBundle} with property files and reads from them in
 * dependence of the current locale.
 * 
 * @author Radoslav
 */
public class ContentLanguageManager {
	private static final Locale BG = new Locale("BG");
	private static ResourceBundle content;
	private static Map<String, Locale> languageSupport = new HashMap<>();
	private static Map<String, String> nextLanguage = new HashMap<>();
	private static String nextAvailableLanguage = "EN";

	static {
		content = ResourceBundle.getBundle(
				"com.sirma.itt.javacourse.chatapp.content", BG);
		languageSupport.put("BG", BG);
		languageSupport.put("EN", Locale.ENGLISH);

		nextLanguage.put("BG", "EN");
		nextLanguage.put("EN", "BG");
	}

	/**
	 * Denies having instances of the class.
	 */
	private ContentLanguageManager() {

	}

	/**
	 * Takes a key and looks it up in the {@link ResourceBundle}.
	 * 
	 * @param key
	 *            The key
	 * @return The value corresponding to the key.
	 */
	public static String getContent(String key) {
		return content.getString(key);
	}

	/**
	 * Changes the locale .
	 * 
	 * @param language
	 *            The languages.
	 */
	public static void setLanguage(String language) {
		content = ResourceBundle.getBundle(
				"com.sirma.itt.javacourse.chatapp.content",
				languageSupport.get(language));
		nextAvailableLanguage = nextLanguage.get(language);
	}

	/**
	 * Gets the next available language.
	 * 
	 * @return String representing the abbreviation of the language.
	 */
	public static String getNextLanguage() {
		return nextAvailableLanguage;
	}

}
