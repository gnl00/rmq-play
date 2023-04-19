package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class RMQClientMain {
    public static void main(String[] args) {
        SpringApplication.run(RMQClientMain.class, args);
    }
}