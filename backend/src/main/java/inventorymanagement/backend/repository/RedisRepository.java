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

    public int del(String... key)
    {
        Jedis jedis = jedisPool.getResource();
        int response = (int) jedis.del(key);
        jedis.close();
        return response;
    }

    public void hmset(String key, Map<String, String> values)
    {
        Jedis jedis = jedisPool.getResource();
        jedis.hmset(key, values);
        jedis.close();
    }

    public String hget(String key, String field)
    {
        Jedis jedis = jedisPool.getResource();
        String response = jedis.hget(key, field);
        jedis.close();
        return response;
    }

    public boolean hexists(String key, String field)
    {
        Jedis jedis = jedisPool.getResource();
        boolean response = jedis.hexists(key, field);
        jedis.close();
        return response;
    }

    public void hdel(String key, String... fields)
    {
        Jedis jedis = jedisPool.getResource();
        jedis.hdel(key, fields);
        jedis.close();
    }

    public int sadd(String key, String... value)
    {
        Jedis jedis = jedisPool.getResource();
        int response = (int) jedis.sadd(key, value);
        jedis.close();
        return response;
    }

    public int srem(String key, String... value)
    {
        Jedis jedis = jedisPool.getResource();
        int response = (int) jedis.srem(key, value);
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

    public void hexpire(String key, int expiration, String... fields) {
        Jedis jedis = jedisPool.getResource();
        jedis.hexpire(key, expiration,  fields);
        jedis.close();
    }

    public int incrby(String key, int value) {
        Jedis jedis = jedisPool.getResource();
        int response = (int) jedis.incrBy(key, value);
        jedis.close();
        return response;
    }
}
