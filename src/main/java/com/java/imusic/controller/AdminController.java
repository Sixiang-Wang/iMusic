package com.java.imusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.imusic.service.AdminService;
import com.java.imusic.service.impl.MailUtil;
import com.java.imusic.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private MailUtil mailUtil;

    /**
     * 判断是否登录成功
     */
    @RequestMapping(value = "/admin/login/status",method = RequestMethod.POST)
    public Object loginStatus(HttpServletRequest request, HttpSession session, HttpServletResponse response){
        JSONObject jsonObject = new JSONObject();
        String name = request.getParameter("name");
        String password = request.getParameter("password");


        boolean flag = adminService.verifyPassword(name,password);
        if(!flag){
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"用户名或密码错误");
            return jsonObject;
        }


        jsonObject.put(Consts.CODE,1);
        jsonObject.put(Consts.MSG,"登录成功");
        session.setAttribute(Consts.NAME,name);

        Cookie cookie_name = new Cookie("cookie_name",name);
        Cookie cookie_password = new Cookie("cookie_password",password);
        cookie_name.setMaxAge(60 * 60);//1h
        cookie_password.setMaxAge(60 * 60);//1h
        cookie_name.setPath(request.getContextPath());
        cookie_password.setPath(request.getContextPath());
        response.addCookie(cookie_name);
        response.addCookie(cookie_password);
        return jsonObject;
    }

    @RequestMapping(value = "/admin/login/preLogin",method = RequestMethod.POST)
    public Object preLogin(HttpServletRequest request){
        mailUtil.sendMail("machenyu2004@163.com");
        JSONObject jsonObject = new JSONObject();
        String name = "";
        String password = "";
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie item:cookies){
                if(item.getName().equals("cookie_name")){
                    name = item.getValue();
                }
                else if(item.getName().equals("cookie_password")){
                    password = item.getValue();
                }
            }
        }
        System.out.println(name+" "+password);
        boolean flag = adminService.verifyPassword(name,password);
        if(!flag){
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"用户名或密码错误");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE,1);
        jsonObject.put(Consts.MSG,"登录成功");
        jsonObject.put("name",name);
        return jsonObject;
    }

    @RequestMapping(value = "/admin/login/logout",method = RequestMethod.POST)
    public Object logout(HttpServletRequest request, HttpSession session, HttpServletResponse response){
        JSONObject jsonObject = new JSONObject();
        session.removeAttribute(Consts.NAME);
        Cookie cookie_password = new Cookie("cookie_password","");
        cookie_password.setMaxAge(0);
        cookie_password.setPath(request.getContextPath());
        response.addCookie(cookie_password);
        jsonObject.put(Consts.CODE,1);
        return jsonObject;
    }



}






















