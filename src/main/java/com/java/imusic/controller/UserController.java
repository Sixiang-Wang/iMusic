package com.java.imusic.controller;


import com.alibaba.fastjson.JSONObject;
import com.java.imusic.domain.Singer;
import com.java.imusic.domain.User;
import com.java.imusic.service.SingerService;
import com.java.imusic.service.UserService;
import com.java.imusic.utils.CipherBean;
import com.java.imusic.utils.Consts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 前端用户控制类
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private CipherBean cipher;
    @Autowired
    private UserService userService;
    @Autowired
    private SingerService singerService;

    /**
     * 添加前端用户
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addUser(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String username = request.getParameter("username").trim();     //账号
        String passwordFont = request.getParameter("password").trim();     //密码
        //加密
        //String password = SaSecureUtil.aesEncrypt(cipher.getKey(), passwordFont); //存储加密后的密码
        String password = request.getParameter("password").trim();
        String sex = request.getParameter("sex").trim();               //性别
        String phoneNum = request.getParameter("phoneNum").trim();     //手机号
        String email = request.getParameter("email").trim();           //电子邮箱
        String birth = request.getParameter("birth").trim();           //生日
        String introduction = request.getParameter("introduction").trim();//签名
        String location = request.getParameter("location").trim();      //地区
        String profilePicture = request.getParameter("profilePicture").trim();          //头像地址
        String name = request.getParameter("name").trim();

        if (username == null || username.equals("")) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "用户名不能为空");
            return jsonObject;
        }

        User user1 = userService.getByUsername(username);
        if (user1 != null) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "用户名已存在");
            return jsonObject;
        }
        User user2 = userService.getUserWithName(name);
        Singer singer = singerService.oneSingerOfName(name);
        if (user2 != null || singer != null) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "昵称已被占用");
            return jsonObject;
        }
        if (password == null || password.equals("")) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "密码不能为空");
            return jsonObject;
        }

        //把生日转换成Date格式
        String tmpDate = request.getParameter("birth").trim();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = new Date();
        if(!tmpDate.equals("undefined")) {
            try {
                birthDate = dateFormat.parse(birth);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            birthDate = null;
        }
        //保存到前端用户的对象中
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setSex(new Byte(sex));
        user.setPhoneNum(phoneNum);
        user.setEmail(email);
        user.setBirth(birthDate);
        user.setIntroduction(introduction);
        user.setLocation(location);
        user.setProfilePicture(profilePicture);
        user.setName(name);
        boolean flag = userService.insert(user);
        if (flag) {   //保存成功
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "添加成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "保存到服务器失败");
        return jsonObject;
    }

    /**
     * 修改前端用户
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object updateUser(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim();          //主键
        String username = request.getParameter("username").trim();     //账号
        String usernameOrigin = request.getParameter("usernameOrigin").trim(); //原来账号
        String password = request.getParameter("password").trim();     //密码
        String sex = request.getParameter("sex").trim();               //性别
        String phoneNum = request.getParameter("phoneNum").trim();     //手机号
        String email = request.getParameter("email").trim();           //电子邮箱
        String birth = request.getParameter("birth").trim();           //生日
        String introduction = request.getParameter("introduction").trim();//签名
        String location = request.getParameter("location").trim();      //地区
        String name = request.getParameter("name").trim();             //昵称
        String nameOrigin = request.getParameter("nameOrigin").trim(); //原来昵称

        if (username == null || username.equals("")) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "用户名不能为空");
            return jsonObject;
        }
        if (password == null || password.equals("")) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "密码不能为空");
            return jsonObject;
        }
        User user1 = userService.getByUsername(username);
        if (user1 != null && !user1.getUsername().equals(usernameOrigin)) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "用户名已存在");
            return jsonObject;
        }
        User user2 = userService.getUserWithName(name);
        Singer singer = singerService.oneSingerOfName(name);
        if ((user2 != null && !user2.getName().equals(nameOrigin)) || (singer != null && !singer.getName().equals(nameOrigin))) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "昵称已被占用");
            return jsonObject;
        }
        //把生日转换成Date格式
        String tmpDate = request.getParameter("birth").trim();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = new Date();
        System.out.println(tmpDate);
        if(!tmpDate.equals("undefined")) {
            try {
                birthDate = dateFormat.parse(birth);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            birthDate = null;
        }
        //保存到前端用户的对象中
        User user = new User();
        user.setId(Integer.parseInt(id));
        user.setUsername(username);
        user.setPassword(password);
        user.setSex(new Byte(sex));
        user.setPhoneNum(phoneNum);
        user.setEmail(email);
        user.setBirth(birthDate);
        user.setIntroduction(introduction);
        user.setLocation(location);
        user.setName(name);
        boolean flag = userService.update(user);
        if (flag) {   //保存成功
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "修改成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "修改失败");
        return jsonObject;
    }


    /**
     * 删除前端用户
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteUser(HttpServletRequest request) {
        String id = request.getParameter("id").trim();          //主键
        boolean flag = userService.delete(Integer.parseInt(id));
        return flag;
    }

    /**
     * 根据主键查询整个对象
     */
    @RequestMapping(value = "/selectByPrimaryKey", method = RequestMethod.GET)
    public Object selectByPrimaryKey(HttpServletRequest request) {
        String id = request.getParameter("id").trim();          //主键
        return userService.selectByPrimaryKey(Integer.parseInt(id));
    }

    /**
     * 查询所有前端用户
     */
    @RequestMapping(value = "/allUser", method = RequestMethod.GET)
    public Object allUser(HttpServletRequest request) {
        return userService.allUser();
    }

    /**
     * 更新前端用户图片
     */
    @RequestMapping(value = "/updateUserPic", method = RequestMethod.POST)
    public Object updateUserPic(@RequestParam("file") MultipartFile profilePictureFile, @RequestParam("id") int id) {
        JSONObject jsonObject = new JSONObject();
        if (profilePictureFile.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "文件上传失败");
            return jsonObject;
        }
        //文件名=当前时间到毫秒+原来的文件名
        String fileName = System.currentTimeMillis() + profilePictureFile.getOriginalFilename();
        //文件路径
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "profilePictureImages";
        //如果文件路径不存在，新增该路径
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }
        //实际的文件地址
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        //存储到数据库里的相对文件地址
        String storeProfilePicturePath = "/profilePictureImages/" + fileName;
        try {
            profilePictureFile.transferTo(dest);
            User user = new User();
            user.setId(id);
            user.setProfilePicture(storeProfilePicturePath);
            boolean flag = userService.update(user);
            if (flag) {
                jsonObject.put(Consts.CODE, 1);
                jsonObject.put(Consts.MSG, "上传成功");
                jsonObject.put("profilePicture", storeProfilePicturePath);
                return jsonObject;
            }
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "上传失败");
            return jsonObject;
        } catch (IOException e) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "上传失败" + e.getMessage());
        } finally {
            return jsonObject;
        }
    }

    /**
     * 前端用户登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        String username = request.getParameter("username");     //账号
        String passwordFont = request.getParameter("password");
        //加密前端传入的 密码
        //根据用户名和密码获取数据库里面所有的信息
        User user = userService.getUserWithAccount(username, passwordFont);
        log.error("数据库中查到的user {}", user);
        //如果查到了用户
        if (user!=null) {
            log.error("=============查询到了user {}", user);

            //设置登录状态
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "登录成功");
        }
        else {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "登录失败");
        }
        return jsonObject;
    }


    /**
     * 二维码手机登录
     *
     * @param phoneNum: 手机号码
     * @return cn.dev33.satoken.util.SaResult
     * @since 2023/3/2 23:12
     */
    @PostMapping("/{phoneNum}")
    public Object loginWithPhoneNum(@PathVariable("phoneNum") String phoneNum, HttpSession session) {
        //只要访问这个接口,就直接先给退出登录,清除cookie
        JSONObject jsonObject = new JSONObject();
        User user = userService.getUserWithPhoneNum(phoneNum);
        if (user!=null) {
            log.error("=============查询到了user {}", user);
            user.setPassword("***");
            //设置登录状态
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "登录成功");
        }
        else {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "登录失败");
        }
        return jsonObject;
    }

    /**
     * 退出登录
     *
     * @param :
     * @return void
     * @since 2023/3/3 22:44
     */
    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        System.out.println("===退出登录===");
        //Cookie[] cookies = request.getCookies();
        //StpUtil.logout();
    }

}
