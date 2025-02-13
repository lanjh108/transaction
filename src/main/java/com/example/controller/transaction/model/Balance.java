package com.example.controller.transaction.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter
@Getter
@TableName("balance")
@ToString
public class Balance {
  @TableId
  private String account;
  private BigDecimal amount;
  private LocalDateTime uptime;
}
