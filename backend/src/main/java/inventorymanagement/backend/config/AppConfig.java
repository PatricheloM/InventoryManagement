package inventorymanagement.backend.config;

import com.ethlo.time.DateTime;
import inventorymanagement.backend.model.RedisConfig;
import inventorymanagement.backend.repository.RedisRepository;
import inventorymanagement.backend.util.auth.AuthorizationCheck;
import inventorymanagement.backend.util.auth.impl.AuthorizationCheckImpl;
import inventorymanagement.backend.util.exception.ConfigNotFoundException;
import inventorymanagement.backend.util.json.ObjectFactory;
import org.modelmapper.AbstractConverter;
import org.modelmapper.AbstractProvider;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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


    Provider<Date> dateProvider = new AbstractProvider<>() {
        @Override
        public Date get() {
            return new Date();
        }
    };

    Converter<String, Date> toStringDate = new AbstractConverter<>() {
        @Override
        protected Date convert(String source) {
            SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            try {
                return sdt.parse(source);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    };

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(String.class, Date.class);
        modelMapper.addConverter(toStringDate);
        modelMapper.getTypeMap(String.class, Date.class).setProvider(dateProvider);
        return modelMapper;
    }

    @Bean
    public AuthorizationCheck authorizationCheck() {
        return new AuthorizationCheckImpl();
    }
}
