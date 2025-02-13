package com.example.controller.transaction.service;

import com.example.controller.transaction.mapper.BalanceMapper;
import com.example.controller.transaction.mapper.TransactionMapper;
import com.example.controller.transaction.model.Balance;
import com.example.controller.transaction.model.Transaction;
import java.time.LocalDateTime;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
public class TransactionService {
  @Autowired
  private TransactionMapper transactionMapper;

  @Autowired
  private BalanceMapper balanceMapper;

  @Autowired
  private RedissonClient redissonClient;

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  public Transaction queryTransaction(String transactionId) {
    Transaction transaction = transactionMapper.selectById(transactionId);
    if (transaction != null) {
      System.out.println("Transaction found: " + transaction);
    } else {
      System.out.println("Transaction not found");
    }
    return transaction;
  }

  @Transactional(rollbackFor = Exception.class)
  @Retryable(
      value = {RuntimeException.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 2000))
  // add retry;
  // concurrent
  public boolean processTransaction(Transaction transaction) {
    String lockKey = "lock:" + transaction.getTransactionId();
    RLock lock = redissonClient.getLock(lockKey);

    try {
      if (lock.tryLock(10, 10, TimeUnit.SECONDS)) {
        // Check if transaction already exists in cache
        // if success, return true; make sure Idempotence
        // 幂等性， cache
        if (redisTemplate.hasKey(transaction.getTransactionId())) {
          return true; // Transaction already processed
        }

        // Check if transaction already exists in database
        // database;
        Transaction tmp = transactionMapper.selectById(transaction.getTransactionId());

        if (tmp != null) {
          // transaction is success;
          redisTemplate.opsForValue().set(transaction.getTransactionId(), transaction);
          return true;
        }

        // 更新amount
        Balance fromBalance = balanceMapper.selectById(transaction.getSourceAccount());

        Balance toBalance = balanceMapper.selectById(transaction.getDestinationAccount());

        if (fromBalance.getAmount().compareTo(transaction.getAmount()) < 0) {
          // 金额不尊
          throw new RuntimeException("Insufficient funds");
        }

        // update in database;
        fromBalance.setAmount(fromBalance.getAmount().subtract(transaction.getAmount()));
        fromBalance.setUptime(LocalDateTime.now());
        // add
        toBalance.setAmount(toBalance.getAmount().add(transaction.getAmount()));
        toBalance.setUptime(LocalDateTime.now());

        balanceMapper.updateById(fromBalance);
        balanceMapper.updateById(toBalance);

        // record transaction
        transactionMapper.insert( transaction);

        // Cache the transaction result
        redisTemplate.opsForValue().set(transaction.getTransactionId(), transaction);
      } else {
        throw new RuntimeException("Could not acquire lock");
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      lock.unlock();
    }
    return true;
  }
}
