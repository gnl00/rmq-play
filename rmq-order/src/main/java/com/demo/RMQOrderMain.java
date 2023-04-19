package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * RMQOrderMain
 *
 * @author gnl
 * @since 2023/4/18
 */
@EnableFeignClients
@SpringBootApplication
public class RMQOrderMain {
    public static void main(String[] args) {
        SpringApplication.run(RMQOrderMain.class, args);
    }
}
