package inventorymanagement.backend.repository;

import org.springframework.data.redis.connection.RedisConnection;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class RedisRepository{

    Charset UTF8 = StandardCharsets.UTF_8;

    RedisConnection connection;

    public RedisRepository(RedisConnection connection) {
        this.connection = connection;
    }

    public boolean set(String key, String value)
    {
        return Boolean.TRUE.equals(connection.set(key.getBytes(UTF8), value.getBytes(UTF8)));
    }

    public Long del(String key)
    {
        return connection.del(key.getBytes(UTF8));
    }

    public void hmset(String key, Map<String, String> values)
    {
        Map<byte[], byte[]> byteValues = new HashMap<>();
        for (String mapKey : values.keySet())
        {
            byteValues.put(mapKey.getBytes(UTF8), values.get(mapKey).getBytes(UTF8));
        }
        connection.hMSet(key.getBytes(UTF8), byteValues);
    }

    public void sadd(String key, String value)
    {
        connection.sAdd(key.getBytes(UTF8), value.getBytes(UTF8));
    }

    public Long srem(String key, String value)
    {
        return connection.sRem(key.getBytes(UTF8), value.getBytes(UTF8));
    }

    public Map<String, String> hgetall(String key)
    {
        Map<byte[], byte[]> byteValues = connection.hGetAll(key.getBytes(UTF8));
        Map<String, String> values = new HashMap<>();

        if (byteValues != null)
        {
            for (byte[] bytes : byteValues.keySet())
            {
                values.put(new String(bytes, UTF8), new String(byteValues.get(bytes), UTF8));
            }
            return values;
        }
        else
        {
            return Collections.emptyMap();
        }
    }

    public List<String> smembers(String key)
    {
        Set<byte[]> byteValues = connection.sMembers(key.getBytes(UTF8));
        List<String> values = new ArrayList<>();
        if (byteValues != null)
        {
            for (byte[] bytes : byteValues)
            {
                values.add(new String(bytes, UTF8));
            }
            return values;
        }
        else
        {
            return Collections.emptyList();
        }
    }

    public boolean sismember(String key, String value)
    {
        return Boolean.TRUE.equals(connection.sIsMember(key.getBytes(UTF8), value.getBytes(UTF8)));
    }

    public boolean setex(String key, String value, int expiration) {
        return Boolean.TRUE.equals(connection.setEx(key.getBytes(UTF8), expiration, value.getBytes(UTF8)));
    }

    public boolean exists(String key) {
        return Boolean.TRUE.equals(connection.exists(key.getBytes(UTF8)));
    }

    public String get(String key) {
        Optional<byte[]> bytes = Optional.ofNullable(connection.get(key.getBytes(UTF8)));
        return bytes.map(String::new).orElse("");
    }

    public Map<String, String> keys(String regex) {
        Map<String, String> map = new HashMap<>();
        for (byte[] bytes : connection.keys(regex.getBytes(UTF8))) {
            String key = new String(bytes, UTF8);
            map.put(key, get(key));
        }
        return map;
    }
}
