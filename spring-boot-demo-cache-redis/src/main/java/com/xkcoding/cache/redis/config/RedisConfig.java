package com.xkcoding.cache.redis.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.time.Duration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.NullValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * <p>
 * redis配置
 * </p>
 *
 * @package: com.xkcoding.cache.redis.config
 * @description: redis配置
 * @author: yangkai.shen
 * @date: Created in 2018/11/15 16:41
 * @copyright: Copyright (c) 2018
 * @version: V1.0
 * @modified: yangkai.shen
 */
@Configuration
//@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig extends CachingConfigurerSupport {

    @Bean("stringRedisSerializer")
    public RedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean("jacksonRedisSerializer")
    public RedisSerializer jacksonRedisSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // 反序列化忽略Json中多余的字段，不会报错
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // simply setting {@code mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)} does not help here since we need
        // the type hint embedded for deserialization using the default typing feature.
        objectMapper.registerModule(new SimpleModule().addSerializer(new NullValueSerializer(null)));
        objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
        return genericJackson2JsonRedisSerializer;
    }

    /**
     * {@link StdSerializer} adding class information required by default typing. This allows de-/serialization of
     * {@link NullValue}.
     *
     * @author Christoph Strobl
     * @since 1.8
     */
    private static class NullValueSerializer extends StdSerializer<NullValue> {

        private static final long serialVersionUID = 1999052150548658808L;
        private final String classIdentifier;

        /**
         * @param classIdentifier can be {@literal null} and will be defaulted to {@code @class}.
         */
        NullValueSerializer(@Nullable String classIdentifier) {

            super(NullValue.class);
            this.classIdentifier = StringUtils.hasText(classIdentifier) ? classIdentifier : "@class";
        }

        /*
         * (non-Javadoc)
         * @see com.fasterxml.jackson.databind.ser.std.StdSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
         */
        @Override
        public void serialize(NullValue value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException {

            jgen.writeStartObject();
            jgen.writeStringField(classIdentifier, NullValue.class.getName());
            jgen.writeEndObject();
        }
    }

    /**
     * 默认情况下的模板只能支持RedisTemplate<String, String>，也就是只能存入字符串，因此支持序列化
     */
    @Bean(name = "redisTemplate")
    @ConditionalOnMissingBean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();

        redisTemplate.setKeySerializer(stringRedisSerializer());
        redisTemplate.setValueSerializer(jacksonRedisSerializer());

        redisTemplate.setHashKeySerializer(stringRedisSerializer());
        redisTemplate.setHashValueSerializer(jacksonRedisSerializer());

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean("redisCacheManager")
    @ConditionalOnMissingBean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(24)); // 设置缓存有效期24小时
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
    }
}
