package com.iot.redis;

import com.iot.common.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author vic
 * @desc redis config bean
 */
@Configuration
@ConfigurationProperties(ignoreInvalidFields = true)
public class RedisConfig {

    public static final int DEFAULT_MAX_IDLE = 8;
    public static final int DEFAULT_MIN_IDLE = 0;
    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.redis.host:}")
    private String host;

    @Value("${spring.redis.port:0}")
    private int port;

    @Value("${spring.redis.database:0}")
    private int database;

    @Value("${spring.redis.timeout:0}")
    private int timeout;

    @Value("${spring.redis.pool.max-idle:8}")
    private int maxIdle;

    @Value("${spring.redis.pool.min-idle:0}")
    private int minIdle;

    @Value("${spring.redis.pool.max-active:8}")
    private int maxActive;

    @Value("${spring.redis.pool.max-wait:-1}")
    private long maxWaitMillis;

    @Value("${spring.redis.password:}")
    private String password;

    /**
     * 连接池配置
     *
     * @return @Description:
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        try {
            JedisPoolConfig jedisPoolConfig = null;
            if (!StringUtils.isEmpty(host)) {
                logger.info("init JredisPool ...");
                jedisPoolConfig = new JedisPoolConfig();
                jedisPoolConfig.setMaxIdle(maxIdle);
                jedisPoolConfig.setMinIdle(minIdle);
                jedisPoolConfig.setMaxTotal(maxActive);
                jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
            }
            return jedisPoolConfig;
        } catch (Exception e) {
            logger.info("init jedisPool error:{}", e);
        }
        return null;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisPoolConfig jedisPoolConfig = jedisPoolConfig();
        logger.info("init jedisConnectionFactory...");
        logger.info("spring.redis.pool.host:{}", this.host);
        logger.info("spring.redis.pool.port:{}", this.port);
        logger.info("spring.redis.pool.timeout:{}", this.timeout);
        logger.info("spring.redis.pool.database:{}", this.database);
        logger.info("spring.redis.pool.max-idle:{}", jedisPoolConfig.getMaxIdle());
        logger.info("spring.redis.pool.min-idle:{}", jedisPoolConfig.getMinIdle());
        logger.info("spring.redis.pool.max-active:{}", jedisPoolConfig.getMaxTotal());
        logger.info("spring.redis.pool.max-wait:{}", jedisPoolConfig.getMaxWaitMillis());
        JedisConnectionFactory factory = new JedisConnectionFactory(jedisPoolConfig);
        factory.setUsePool(true);
        factory.setHostName(this.host);
        factory.setPort(this.port);
        factory.setTimeout(this.timeout);
        factory.setDatabase(this.database);

        if (StringUtil.isNotEmpty(this.password)) {
            factory.setPassword(this.password);
        }

        return factory;
    }

    @Bean("StringRedisTemplate")
    public RedisTemplate stringRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());

        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);

        return redisTemplate;
    }

    @Bean
    public JedisPool redisPoolFactory() {
        try {

            JedisPoolConfig jedisPoolConfig = jedisPoolConfig();
            if (jedisPoolConfig != null) {
                logger.info("JedisPool注入成功！！");
                logger.info("spring.redis.pool.host:{}", this.host);
                logger.info("spring.redis.pool.port:{}", this.port);
                logger.info("spring.redis.pool.timeout:{}", this.timeout);
                logger.info("spring.redis.pool.database:{}", this.database);
                logger.info("spring.redis.pool.max-idle:{}", jedisPoolConfig.getMaxIdle());
                logger.info("spring.redis.pool.min-idle:{}", jedisPoolConfig.getMinIdle());
                logger.info("spring.redis.pool.max-active:{}", jedisPoolConfig.getMaxTotal());
                logger.info("spring.redis.pool.max-wait:{}", jedisPoolConfig.getMaxWaitMillis());
                JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, database);
                return jedisPool;
            }
        } catch (Exception e) {
            logger.info("jedisPool--init--error", e);
        }
        return null;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public long getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

