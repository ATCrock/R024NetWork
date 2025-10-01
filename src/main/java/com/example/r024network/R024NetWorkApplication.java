package com.example.r024network;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.example.r024network.mapper")
@SpringBootApplication
@EnableScheduling
public class R024NetWorkApplication {
    public static void main(String[] args) {
        SpringApplication.run(R024NetWorkApplication.class, args);
    }

}
