package com.mivim.common.nosql.redis;

import org.ofbiz.base.util.UtilProperties;

import redis.clients.jedis.JedisPool;

public enum RedisConnectionFactory {
	CONNECTION;
	private JedisPool client = null;

	private RedisConnectionFactory() {
		try {
			String ip = UtilProperties.getPropertyValue("admin.properties", "redis.ip");
			Integer port = Integer.parseInt(UtilProperties.getPropertyValue("admin.properties", "redis.port"));

			// public String resource = "admin.properties";
			// need to change ip and port in production
			client = new JedisPool(ip, port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JedisPool getClient() {
		if (client == null)
			throw new RuntimeException();
		return client;
	}
}
