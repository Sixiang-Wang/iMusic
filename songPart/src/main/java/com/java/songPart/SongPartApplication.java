package com.java.songPart;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@MapperScan("com.java.songPart.dao")
public class SongPartApplication {

    public static void main(String[] args) {
        SpringApplication.run(SongPartApplication.class, args);
    }

    //实现远程调用
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
