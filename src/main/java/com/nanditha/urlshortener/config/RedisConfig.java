package com.nanditha.urlshortener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * Provides a RedisTemplate for storing and retrieving string key-value pairs.
     * Spring Boot auto-configures the RedisConnectionFactory from application.properties.
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Serialize keys as plain strings so they are readable in Redis CLI and tools.
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);

        // Serialize values as plain strings for simple cache lookups (e.g. shortCode -> originalUrl).
        template.setValueSerializer(stringSerializer);

        // Apply serializers before the template is used.
        template.afterPropertiesSet();
        return template;
    }
}
