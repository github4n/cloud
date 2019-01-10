package com.iot.video.util;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

@Component
public class JedisUtil {
	
	public Jedis jedis;

	public void close() {
		jedis.disconnect();
		jedis = null;
	}

	@SuppressWarnings({ "deprecation", "resource" })
	public Jedis open(String host, int port, String password) {
		JedisPoolConfig config = new JedisPoolConfig();

		JedisPool pool;
		pool = new JedisPool(config, host, port, 2000, password);

		boolean borrowOrOprSuccess = true;
		try {
			jedis = pool.getResource();
		} catch (JedisConnectionException e) {
			borrowOrOprSuccess = false;
			if (jedis != null)
				pool.returnBrokenResource(jedis);

		} finally {
			if (borrowOrOprSuccess)
				pool.returnResource(jedis);
		}
		jedis = pool.getResource();
		return jedis;
	}
    
}
