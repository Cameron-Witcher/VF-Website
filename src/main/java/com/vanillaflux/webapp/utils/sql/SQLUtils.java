package com.vanillaflux.webapp.utils.sql;

import java.util.HashMap;
import java.util.Map;

public class SQLUtils {

	private static Map<String, IDatabase> databases = new HashMap<>();

	public static boolean createDatabase(String name, IDatabase db) {
		if (!db.init()) {
			System.out.println("There was an error registering database: " + name);
			return false;
		}
		databases.put(name, db);
		return true;
	}

	public static void createDatabase(String name, SQLDriver driver, String host, String database, Integer port,
			String username, String password) {
		createDatabase(name, new IDatabase(driver, host, database, port, username, password));

	}

	public static IDatabase getDatabase(String name) {
		return databases.containsKey(name) ? databases.get(name) : null;
	}

}
