package com.java.messagepart;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.java.messagepart.dao")
public class MessagePartApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessagePartApplication.class, args);
    }

}
