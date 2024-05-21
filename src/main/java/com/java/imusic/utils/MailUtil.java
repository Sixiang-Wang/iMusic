package com.java.imusic.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.internet.InternetAddress;
import java.util.Random;


@Component
public class MailUtil {
    private final Random random = new Random();

    private final String myMailAddress = "2562187628@qq.com";

    @Autowired
    private JavaMailSender javaMailSender;
    public String sendMail(String to){
        String verifyCode = getVerifyCode();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("验证码");
        msg.setText("iMusic验证码为:"+verifyCode);
        msg.setFrom("iMusic官方 <"+myMailAddress+">");
        /*
        msg.setSubject("你爹来咯");
        msg.setText("我是你爹");
        msg.setFrom("爹 <"+myMailAddress+">");
        */
        javaMailSender.send(msg);
        return verifyCode;
    }

    public void sendCustomMail(String to,String from,String subject,String text){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        msg.setFrom(from+" <"+myMailAddress+">");
        javaMailSender.send(msg);
    }

    public String getVerifyCode(){
        Integer randomNum = random.nextInt(999999);
        return String.format("%06d",randomNum);
    }

}
