package com.example.controller.transaction.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Data
@TableName("transactions")
public class Transaction {
  @TableId
  private String transactionId;
  private String sourceAccount;
  private String destinationAccount;
  private BigDecimal amount;
  private LocalDateTime uptime;
}
