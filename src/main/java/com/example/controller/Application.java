package com.example.controller;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.controller.transaction.mapper")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
