package common;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by lansi on 2015/8/16.
 */
public class SpringRedisBuilder {
    public static RedisTemplate build() {
        JedisPoolConfig jpc = new JedisPoolConfig();
        jpc.setMaxIdle(5);
        jpc.setMaxTotal(10);
        jpc.setMinIdle(3);
        jpc.setMaxWaitMillis(1000);
        JedisConnectionFactory jcf = new JedisConnectionFactory(jpc);
        jcf.setHostName("192.168.199.208");
        jcf.setPort(7777);
        jcf.setTimeout(3000);
        jcf.setUsePool(true);
        jcf.afterPropertiesSet();
        RedisTemplate<String, String> redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jcf);
        redisTemplate.setEnableDefaultSerializer(true);
//        redisTemplate.setKeySerializer(redisTemplate.getDefaultSerializer());
//        redisTemplate.setValueSerializer(redisTemplate.getDefaultSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
