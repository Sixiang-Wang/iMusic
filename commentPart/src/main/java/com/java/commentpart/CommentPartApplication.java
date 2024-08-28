package com.java.commentpart;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
@MapperScan("com.java.commentpart.dao")
public class CommentPartApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommentPartApplication.class, args);
    }

}
