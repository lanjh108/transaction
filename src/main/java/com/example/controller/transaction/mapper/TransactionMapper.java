package com.example.controller.transaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.controller.transaction.model.Transaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionMapper extends BaseMapper<Transaction> {
}

