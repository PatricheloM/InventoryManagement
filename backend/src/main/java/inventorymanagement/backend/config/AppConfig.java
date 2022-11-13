package inventorymanagement.backend.config;

import inventorymanagement.backend.model.RedisConfig;
import inventorymanagement.backend.repository.RedisRepository;
import inventorymanagement.backend.util.exception.ConfigNotFoundException;
import inventorymanagement.backend.util.json.ObjectFactory;
import inventorymanagement.backend.util.session.Session;
import inventorymanagement.backend.util.session.impl.SessionImpl;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.io.IOException;

@Configuration
@EnableCaching
public class AppConfig {

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisConfig redisConfig;
        try {
            redisConfig = ObjectFactory.produce(AppConfig.class.getResourceAsStream("redis.json"), RedisConfig.class);
        } catch (Exception e) {
            throw new ConfigNotFoundException(e);
        }
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisConfig.getHost(), redisConfig.getPort());
        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisConfig.getPassword()));
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisRepository redisRepository() {
        return new RedisRepository(jedisConnectionFactory().getConnection());
    }

    @Bean
    public Session session() {
        return new SessionImpl();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
