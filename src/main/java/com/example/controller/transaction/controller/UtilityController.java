package com.example.controller.transaction.controller;

import com.example.controller.transaction.mapper.BalanceMapper;
import com.example.controller.transaction.mapper.TransactionMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utility")
public class UtilityController {
  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  @Autowired
  private BalanceMapper balanceMapper;

  @Autowired
  private TransactionMapper transactionMapper;

  @GetMapping("/check_redis")
  public String checkRedis() {
    System.out.println("Redis is working!");
    redisTemplate.opsForValue().set("test", "test");
    System.out.println(redisTemplate.opsForValue().get("test"));
    System.out.println(redisTemplate.opsForValue().get("owen"));
    return "success";
  }

  @GetMapping("/check_db")
  public String checkDb() {
    System.out.println("db");
    System.out.println(balanceMapper.selectById("owen"));
    return "success";
  }
}
