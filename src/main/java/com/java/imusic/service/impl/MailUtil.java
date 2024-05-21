package com.java.imusic.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.internet.InternetAddress;


@Component
public class MailUtil {

    private String myMailAddress = "2562187628@qq.com";
    @Autowired
    private JavaMailSender javaMailSender;
    public void sendMail(String to){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("验证码");
        msg.setText("你先当我是验证码");
        msg.setFrom("iMusic官方 <"+myMailAddress+">");
        javaMailSender.send(msg);
    }

}
