package inventorymanagement.backend.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;


@Repository
public class RedisRepository {

    @Autowired
    JedisPool jedisPool;

    public void set(String key, String value)
    {
        Jedis jedis = jedisPool.getResource();
        jedis.set(key, value);
        jedis.close();
    }

    public int del(String... key)
    {
        Jedis jedis = jedisPool.getResource();
        int response = jedis.del(key).intValue();
        jedis.close();
        return response;
    }

    public void hmset(String key, Map<String, String> values)
    {
        Jedis jedis = jedisPool.getResource();
        jedis.hmset(key, values);
        jedis.close();
    }

    public int sadd(String key, String... value)
    {
        Jedis jedis = jedisPool.getResource();
        int response = jedis.sadd(key, value).intValue();
        jedis.close();
        return response;
    }

    public int srem(String key, String... value)
    {
        Jedis jedis = jedisPool.getResource();
        int response = jedis.srem(key, value).intValue();
        jedis.close();
        return response;
    }

    public Map<String, String> hgetall(String key)
    {
        Jedis jedis = jedisPool.getResource();
        Map<String, String> values = jedis.hgetAll(key);
        jedis.close();
        if (!values.isEmpty()) {
            return values;
        } else {
            return Collections.emptyMap();
        }
    }

    public List<String> smembers(String key)
    {
        Jedis jedis = jedisPool.getResource();
        List<String> values = new ArrayList<>(jedis.smembers(key));
        jedis.close();
        if (!values.isEmpty()) {
            return values;
        } else {
            return Collections.emptyList();
        }
    }

    public boolean sismember(String key, String value)
    {
        Jedis jedis = jedisPool.getResource();
        boolean response = jedis.sismember(key, value);
        jedis.close();
        return response;
    }

    public void setex(String key, String value, int expiration) {
        Jedis jedis = jedisPool.getResource();
        jedis.setex(key, expiration, value);
        jedis.close();
    }

    public boolean exists(String key) {
        Jedis jedis = jedisPool.getResource();
        boolean response = jedis.exists(key);
        jedis.close();
        return response;
    }

    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        String response = jedis.get(key);
        jedis.close();
        return response;
    }

    public List<String> keys(String regex) {
        Jedis jedis = jedisPool.getResource();
        List<String> response = new ArrayList<>(jedis.keys(regex));
        jedis.close();
        return response;
    }

    public int incrby(String key, int value) {
        Jedis jedis = jedisPool.getResource();
        int response = jedis.incrBy(key, value).intValue();
        jedis.close();
        return response;
    }
}
