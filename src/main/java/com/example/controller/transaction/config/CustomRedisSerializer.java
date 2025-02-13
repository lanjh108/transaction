package com.example.controller.transaction.config;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

public class CustomRedisSerializer implements RedisSerializer<Object> {
  private final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
  private final GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
  private final JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();

  @Override
  public byte[] serialize(Object value) throws SerializationException {
    if (value instanceof String) {
      return stringRedisSerializer.serialize((String) value);
    } else if (value instanceof Number || value instanceof Boolean) {
      return stringRedisSerializer.serialize(value.toString());
    } else {
      try {
        return jsonRedisSerializer.serialize(value);
      } catch (Exception e) {
        return jdkSerializationRedisSerializer.serialize(value);
      }
    }
  }

  @Override
  public Object deserialize(byte[] bytes) throws SerializationException {
    if (bytes == null || bytes.length == 0) {
      return null;
    }
    try {
      String str = stringRedisSerializer.deserialize(bytes);
      if (str != null) {
        return str;
      }
    } catch (Exception ignored) {
    }
    try {
      return jsonRedisSerializer.deserialize(bytes);
    } catch (Exception ignored) {
    }
    return jdkSerializationRedisSerializer.deserialize(bytes);
  }
}
