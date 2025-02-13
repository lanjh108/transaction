package com.example.controller.transaction.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Value("${spring.redis.host}")
  private String redisHost;

  @Value("${spring.redis.port}")
  private String redisPort;


  @Value("${spring.redis.password}")
  private String redisPassword;

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);

    // Use StringRedisSerializer for key serialization
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());

    // Use CustomRedisSerializer for value serialization
    CustomRedisSerializer customRedisSerializer = new CustomRedisSerializer();
    redisTemplate.setValueSerializer(customRedisSerializer);
    redisTemplate.setHashValueSerializer(customRedisSerializer);

    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

  @Bean(destroyMethod = "shutdown")
  public RedissonClient redissonClient() {
    Config config = new Config();
    config.useSingleServer()
        .setAddress("redis://" + redisHost + ":" + redisPort)
        .setPassword(redisPassword)
        .setTimeout(10000)  // 设置命令超时时间为 10000 毫秒
        .setConnectTimeout(15000)  // 设置连接超时时间为 15000 毫秒
        .setRetryAttempts(5)  // 设置重试次数
        .setRetryInterval(2000)  // 设置重试间隔时间
        .setConnectionMinimumIdleSize(1)  // 设置最小空闲连接数
        .setConnectionPoolSize(64)  // 设置连接池大小
        .setSubscriptionConnectionMinimumIdleSize(1)  // 设置最小空闲订阅连接数
        .setSubscriptionConnectionPoolSize(50);  // 设置订阅连接池大小
    return Redisson.create(config);
  }
}
