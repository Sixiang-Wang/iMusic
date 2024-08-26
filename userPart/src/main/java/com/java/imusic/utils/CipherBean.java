package com.java.imusic.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 秘钥
 *
 * @since 2023/3/2 17:47   @author kkl  @version 1.00
 */
@ConfigurationProperties(prefix = "cipher")
@Component
@Data
public class CipherBean {
    /*秘钥*/
    private String key;
}
