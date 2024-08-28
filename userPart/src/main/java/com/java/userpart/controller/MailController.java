package com.java.userpart.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.userpart.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MailController {
    @Autowired
    MailUtil mailUtil;

    @RequestMapping(value = "/mail/sendMail",method = RequestMethod.GET)
    public Object sendMail(HttpServletRequest request){
        String to = request.getParameter("to").trim();
        JSONObject jsonObject = new JSONObject();
        String verifyCode = mailUtil.sendMail(to);
        jsonObject.put("verifyCode",verifyCode);
        System.out.println(to+":"+verifyCode);
        return jsonObject;
    }

}
