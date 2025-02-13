package com.example.controller.transaction.controller;

import com.example.controller.transaction.model.Transaction;
import com.example.controller.transaction.service.TransactionService;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONObject;


@RestController
  @RequestMapping("/transactions")
public class TransactionController {

  @Autowired
  private TransactionService transactionService;

  @PostMapping
  @ResponseBody
  public String createTransaction(@RequestBody Transaction transaction) {
    transaction.setTransactionId(UUID.randomUUID().toString());
    transaction.setUptime(LocalDateTime.now());
    try {
      boolean status = transactionService.processTransaction(transaction);
      JSONObject json = new JSONObject();
      json.put("result", status);
      return json.toJSONString();
    } catch (RuntimeException exception) {
      JSONObject json = new JSONObject();
      json.put("result", false);
      json.put("message", exception.getMessage());
      return json.toJSONString();
    }
  }
}
