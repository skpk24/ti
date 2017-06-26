package com.mivim.component.loader;

import org.ofbiz.base.container.Container;
import org.ofbiz.base.container.ContainerConfig;
import org.ofbiz.base.container.ContainerException;
import org.ofbiz.base.util.Debug;

import com.mivim.common.nosql.redis.RedisConnectionFactory;

import redis.clients.jedis.JedisPool;

/**
 * A container for Redis.
 */
public class RedisContainer implements Container {

	private static final String module = RedisContainer.class.getName();

	public static JedisPool client = null;

	protected String containerName;

	@Override
	public void init(String[] args, String name, String configFile) throws ContainerException {

		Debug.logInfo("Initializing redis container", module);

		this.containerName = name;

		/*
		 * ContainerConfig.Container cfg = ContainerConfig.getContainer(name,
		 * configFile); ContainerConfig.Container.Property ip =
		 * cfg.getProperty("ip"); ContainerConfig.Container.Property port =
		 * cfg.getProperty("port");
		 */
		client = RedisConnectionFactory.CONNECTION.getClient();

	}

	@Override
	public boolean start() throws ContainerException {
		Debug.logInfo("Starting redis container", module);

		return true;
	}

	@Override
	public void stop() throws ContainerException {
		Debug.logInfo("Stopping redis container", module);

		try {
			if (client != null) {

				client.destroy();
			}

		} catch (Exception e) {
			throw new ContainerException(e);
		}
	}

	public String getName() {
		return containerName;
	}
}
