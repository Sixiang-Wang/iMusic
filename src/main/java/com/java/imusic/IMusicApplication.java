package com.java.imusic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.java.imusic.dao")
public class IMusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(IMusicApplication.class, args);
    }

}
