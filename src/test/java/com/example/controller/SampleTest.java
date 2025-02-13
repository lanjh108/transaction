package com.example.controller;

import com.example.controller.transaction.mapper.BalanceMapper;
import com.example.controller.transaction.mapper.TransactionMapper;
import com.example.controller.transaction.model.Transaction;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// unit test
// intergeration test
// performance test   jemeter call {ip}:8080/transactions
// resilense test
@SpringBootTest
public class SampleTest {
  @Autowired
  private TransactionMapper transactionMapper;

  @Autowired
  private BalanceMapper balanceMapper;

  @Test
  public void testSelect() {
    System.out.println(("----- selectAll method test ------"));
    List<Transaction> transactionList = transactionMapper.selectList(null);
    transactionList.forEach(System.out::println);

    System.out.println(transactionMapper.selectById("owen1234"));
    System.out.println(balanceMapper.selectById("owen"));
  }

}