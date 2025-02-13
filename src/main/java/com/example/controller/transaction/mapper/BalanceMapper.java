package com.example.controller.transaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.controller.transaction.model.Balance;
import com.example.controller.transaction.model.Transaction;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BalanceMapper extends BaseMapper<Balance> {
}

