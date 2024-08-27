package com.java.songPart;

import com.java.*;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.java.songPart.dao")
public class SongPartApplication {
    public static void main(String[] args) {
        SpringApplication.run(SongPartApplication.class, args);
    }

}
