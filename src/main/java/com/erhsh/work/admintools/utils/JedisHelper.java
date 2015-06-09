package com.erhsh.work.admintools.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisHelper {
	private static final JedisPool pool;
	private static final int ONE_SECOND = 10000;

	static {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(10000);
		poolConfig.setMaxWaitMillis(ONE_SECOND);

//		String host = "localhost";
		 String host = "192.168.2.14";
		int port = 6379;
		String password = null;

		pool = new JedisPool(poolConfig, host, port, ONE_SECOND, password);
	}

	public static Jedis getJedis() {
		return pool.getResource();
	}

	public static void returnJedis(Jedis res) {
		if (res != null && res.isConnected()) {
			pool.returnResource(res);
		}
	}

	public static void returnBrokenJedis(Jedis res) {
		if (res != null && res.isConnected()) {
			pool.returnBrokenResource(res);
		}
	}

}
