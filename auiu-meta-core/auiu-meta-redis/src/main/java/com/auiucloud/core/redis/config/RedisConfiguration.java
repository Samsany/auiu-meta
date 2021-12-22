package com.auiucloud.core.redis.config;

import com.auiucloud.core.redis.core.RedisService;
import com.auiucloud.core.redis.core.impl.RedisServiceImpl;
import com.auiucloud.core.redis.props.MetaRedisProps;
import com.auiucloud.core.redis.utils.RedisLockUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis基础配置类
 *
 * @author dries
 * @date 2021/12/22
 */
@Configuration
@EnableConfigurationProperties(MetaRedisProps.class)
@ConditionalOnProperty(prefix = "auiu-cloud.redis", value = "enabled", havingValue = "true", matchIfMissing = true)
public class RedisConfiguration {

    @SuppressWarnings("all")
    @Bean(name = "redisTemplate")
    @ConditionalOnClass(RedisOperations.class)
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisSerializer<Object> serializer = redisSerializer();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // key采用String的序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // value序列化方式采用jackson
        redisTemplate.setValueSerializer(serializer);
        // hash的key采用String的序列化方式
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // value序列化方式采用jackson
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisSerializer<Object> redisSerializer() {
        // 创建JSON序列化器
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 必须设置，否则无法将JSON转化为对象，会转化成Map类型
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

    @Bean
    @ConditionalOnBean(name = "redisTemplate")
    public RedisService redisService() {
        return new RedisServiceImpl();
    }

    @Bean
    @ConditionalOnBean(name = "redisTemplate")
    public RedisLockUtil redisLockUtil(RedisTemplate<String, Object> redisTemplate) {
        return new RedisLockUtil(redisTemplate);
    }

//    @Bean
//    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
//        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
//        // 设置Redis缓存有效期为1天
//        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer())).entryTtl(Duration.ofDays(1));
//        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
//    }

}
