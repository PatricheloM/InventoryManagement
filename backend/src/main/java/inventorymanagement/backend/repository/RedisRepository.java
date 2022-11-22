package inventorymanagement.backend.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisPool;

import java.util.*;


@Repository
public class RedisRepository{

    @Autowired
    JedisPool jedisPool;

    public void set(String key, String value)
    {
        jedisPool.getResource().set(key, value);
    }

    public int del(String... key)
    {
        return jedisPool.getResource().del(key).intValue();
    }

    public void hmset(String key, Map<String, String> values)
    {
        jedisPool.getResource().hmset(key, values);
    }

    public int sadd(String key, String... value)
    {
        return jedisPool.getResource().sadd(key, value).intValue();
    }

    public int srem(String key, String... value)
    {
        return jedisPool.getResource().srem(key, value).intValue();
    }

    public Map<String, String> hgetall(String key)
    {
        Map<String, String> values = jedisPool.getResource().hgetAll(key);
        if (!values.isEmpty()) {
            return values;
        } else {
            return Collections.emptyMap();
        }
    }

    public List<String> smembers(String key)
    {
        List<String> values = new ArrayList<>(jedisPool.getResource().smembers(key));
        if (!values.isEmpty()) {
            return values;
        } else {
            return Collections.emptyList();
        }
    }

    public boolean sismember(String key, String value)
    {
        return jedisPool.getResource().sismember(key, value);
    }

    public void setex(String key, String value, int expiration) {
        jedisPool.getResource().setex(key, expiration, value);
    }

    public boolean exists(String key) {
        return jedisPool.getResource().exists(key);
    }

    public String get(String key) {
        return jedisPool.getResource().get(key);
    }

    public List<String> keys(String regex) {
        return new ArrayList<>(jedisPool.getResource().keys(regex));
    }

    public int incrby(String key, int value) {
        return jedisPool.getResource().incrBy(key, value).intValue();
    }
}
