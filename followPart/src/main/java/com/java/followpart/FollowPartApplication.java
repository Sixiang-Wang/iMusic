package com.java.followpart;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.java.followpart.dao")
public class FollowPartApplication {

    public static void main(String[] args) {
        SpringApplication.run(FollowPartApplication.class, args);
    }

}
