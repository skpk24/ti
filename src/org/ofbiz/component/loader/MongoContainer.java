package com.mivim.component.loader;

import org.ofbiz.base.container.Container;
import org.ofbiz.base.container.ContainerException;
import org.ofbiz.base.util.Debug;

import com.mivim.common.nosql.mongodb.MongoConnectionFactory;
import com.mongodb.MongoClient;

/**
 * A container for mongodb.
 */
public class MongoContainer implements Container {

	private static final String module = MongoContainer.class.getName();

	public static MongoClient client = null;

	protected String containerName;

	@Override
	public void init(String[] args, String name, String configFile) throws ContainerException {

		Debug.logInfo("Initializing mongodb container", module);

		this.containerName = name;

		/*
		 * ContainerConfig.Container cfg = ContainerConfig.getContainer(name,
		 * configFile); ContainerConfig.Container.Property ip =
		 * cfg.getProperty("ip"); ContainerConfig.Container.Property port =
		 * cfg.getProperty("port");
		 */
		client = MongoConnectionFactory.CONNECTION.getClient();

	}

	@Override
	public boolean start() throws ContainerException {
		Debug.logInfo("Starting mongodb container", module);

		return true;
	}

	@Override
	public void stop() throws ContainerException {
		Debug.logInfo("Stopping mongodb container", module);

		try {
			if (client != null) {

				client.close();
			}

		} catch (Exception e) {
			throw new ContainerException(e);
		}
	}

	public String getName() {
		return containerName;
	}
}
