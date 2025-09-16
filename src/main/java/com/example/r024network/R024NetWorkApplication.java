package com.example.r024network;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.example.r024network.mapper")
@SpringBootApplication
public class R024NetWorkApplication {
    public static void main(String[] args) {
        SpringApplication.run(R024NetWorkApplication.class, args);
    }

}
