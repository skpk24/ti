package com.mivim.common.nosql.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 
 * @author Ramesh Papaganti
 * 
 **/
public class RedisOpts {

	private static JedisPool pool = null;

	static {
		pool = RedisConnectionFactory.CONNECTION.getClient();
	}

	private RedisOpts() {
	}

	public static void add(String key, Set<String> values) {

		Jedis jedis = borrow();
		try {
			jedis.sadd(key, values.toArray(new String[0]));

			Set<String> members = jedis.smembers(key);
			for (String member : members) {
				System.out.println(member);
			}
		} finally {
			revert(jedis);
		}
	}

	public static Set<String> getSet(String key) {

		Set<String> members = null;

		Jedis jedis = borrow();
		try {
			members = jedis.smembers(key);

		} finally {
			revert(jedis);
		}
		return members;
	}

	public static void add(String key, Map<String, String> map) {

		Jedis jedis = pool.getResource();
		try {

			jedis.hmset(key, map);

		} finally {
			revert(jedis);
		}
	}

	public static Map<String, String> getMap(String key) {
		Map<String, String> retrieveMap = null;
		Jedis jedis = borrow();
		try {
			retrieveMap = jedis.hgetAll(key);
		} finally {
			revert(jedis);
		}
		return retrieveMap;
	}

	public static void add(String key, List<String> values) {

		Jedis jedis = borrow();
		try {

			jedis.lpush(key, values.toArray(new String[0]));

		} finally {
			revert(jedis);
		}
	}

	public static List<String> getList(String key) {

		Jedis jedis = borrow();
		try {

			return jedis.lrange(key, 0, -1);

		} finally {
			revert(jedis);
		}
	}

	public static boolean contains(String key) {
		Jedis jedis = borrow();
		try {
			return jedis.exists(key);
		} finally {
			revert(jedis);
		}
	}

	public static void delete(String key) {
		Jedis jedis = borrow();
		try {
			jedis.del(key);
		} finally {
			revert(jedis);
		}
	}

	public static String set(String key, String value) {
		Jedis jedis = borrow();
		try {
			return jedis.set(key, value);
		} finally {
			revert(jedis);
		}
	}
	
	public static String getType(String key) {
		Jedis jedis = borrow();
		try {
			return jedis.type(key);
		} finally {
			revert(jedis);
		}
	}

	public static void destory() {
		pool.destroy();
	}

	private static Jedis borrow() {
		return pool.getResource();
	}

	private static void revert(Jedis jedis) {
		pool.returnResource(jedis);
	}

}
