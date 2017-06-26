package com.mivim.common.nosql.mongodb;

import com.mongodb.MongoClient;
import org.ofbiz.base.util.*;
import java.util.Properties;

public enum MongoConnectionFactory {
	CONNECTION;
	private MongoClient client = null;

	private MongoConnectionFactory() {
		try {

			if (client == null) {
				String ip = UtilProperties.getPropertyValue("admin.properties", "mongo.ip");
				Integer port = Integer.parseInt(UtilProperties.getPropertyValue("admin.properties", "mongo.port"));

				// public String resource = "admin.properties";
				// need to change ip and port in production
				client = new MongoClient(ip, port);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MongoClient getClient() {
		if (client == null)
			throw new RuntimeException();
		return client;
	}
}
