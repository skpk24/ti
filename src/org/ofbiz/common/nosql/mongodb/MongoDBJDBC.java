package com.mivim.common.nosql.mongodb;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoDBJDBC {

	public static DB getMongoDataBase(String dbName, String user, String password) {
		DB db = null;
		try {
			// To connect to mongodb server
			MongoClient mongoClient = MongoConnectionFactory.CONNECTION.getClient();

			// Now connect to your databases
			db = mongoClient.getDB(dbName);
			// boolean auth = db.authenticate("user", "user");
			// System.out.println("Authentication: "+auth);

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return db;
	}

}