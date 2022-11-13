package inventorymanagement.backend.config;

import inventorymanagement.backend.model.RedisConfig;
import inventorymanagement.backend.repository.RedisRepository;
import inventorymanagement.backend.util.auth.AuthorizationCheck;
import inventorymanagement.backend.util.auth.impl.AuthorizationCheckImpl;
import inventorymanagement.backend.util.exception.ConfigNotFoundException;
import inventorymanagement.backend.util.json.ObjectFactory;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

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
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public AuthorizationCheck authorizationCheck() {
        return new AuthorizationCheckImpl();
    }
}
