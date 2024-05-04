package com.java.imusic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("img/singerPic/**").addResourceLocations(
                "file:"+System.getProperty("user.dir")+System.getProperty("file.separator")+"img" +System.getProperty("file.separator")+"singerPic"+System.getProperty("file.separator")
        );

        registry.addResourceHandler("img/songPic/**").addResourceLocations(
                "file:"+System.getProperty("user.dir")+System.getProperty("file.separator")+"img" +System.getProperty("file.separator")+"songPic"+System.getProperty("file.separator")
        );

        registry.addResourceHandler("img/songListPic/**").addResourceLocations(
                "file:"+System.getProperty("user.dir")+System.getProperty("file.separator")+"img" +System.getProperty("file.separator")+"songListPic"+System.getProperty("file.separator")
        );

        registry.addResourceHandler("song/**").addResourceLocations(
                "file:"+System.getProperty("user.dir")+System.getProperty("file.separator")+"song"+System.getProperty("file.separator")
        );

        registry.addResourceHandler("profilePictureImages/**").addResourceLocations(
                "file:"+System.getProperty("user.dir")+System.getProperty("file.separator")+"profilePictureImages"+System.getProperty("file.separator")
        );
    }
}
