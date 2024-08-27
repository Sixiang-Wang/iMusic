package com.java.imusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.imusic.service.AdminService;
import com.java.imusic.utils.Consts;
import com.java.imusic.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class AdminController {

    @Autowired
    private AdminService adminService;

    private static final String ALGORITHM = "AES";

    /**
     * 判断是否登录成功
     */
    @RequestMapping(value = "/admin/login/status",method = RequestMethod.POST)
    public Object loginStatus(HttpServletRequest request, HttpSession session, HttpServletResponse response){
        JSONObject jsonObject = new JSONObject();
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String cypher = SecurityUtil.encrypt(password);

        boolean flag = adminService.verifyPassword(name,cypher);
        if(!flag){
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"用户名或密码错误");
            return jsonObject;
        }


        jsonObject.put(Consts.CODE,1);
        jsonObject.put(Consts.MSG,"登录成功");
        session.setAttribute(Consts.NAME,name);

        Cookie cookie_name = new Cookie("cookie_name",name);
        Cookie cookie_cypher = new Cookie("cookie_cypher",cypher);
        cookie_name.setMaxAge(24 * 60 * 60);
        cookie_cypher.setMaxAge(24 * 60 * 60);
        cookie_name.setPath(request.getContextPath());
        cookie_cypher.setPath(request.getContextPath());
        response.addCookie(cookie_name);
        response.addCookie(cookie_cypher);
        return jsonObject;
    }

    @RequestMapping(value = "/admin/login/preLogin",method = RequestMethod.POST)
    public Object preLogin(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        String name = "";
        String cypher = "";
        String cookiesTmp = request.getHeader("cookie");
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie item:cookies){
                if("cookie_name".equals(item.getName())){
                    name = item.getValue();
                }
                else if("cookie_cypher".equals(item.getName())){
                    cypher = item.getValue();
                }
            }
        }
        System.out.println(name+" "+cypher);
        boolean flag = adminService.verifyPassword(name,cypher);
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
        Cookie cookie_cypher = new Cookie("cookie_cypher","");
        cookie_cypher.setMaxAge(0);
        cookie_cypher.setPath(request.getContextPath());
        response.addCookie(cookie_cypher);
        jsonObject.put(Consts.CODE,1);
        return jsonObject;
    }

    @RequestMapping(value = "/test",method = RequestMethod.POST)
    public Object test(HttpServletRequest request) {
        String password = request.getParameter("password");
        return SecurityUtil.encrypt(password);
    }

    @RequestMapping(value = "/test2",method = RequestMethod.POST)
    public Object test2(HttpServletRequest request){
        String password = request.getParameter("password");
        return SecurityUtil.decrypt(password);
    }

}






















