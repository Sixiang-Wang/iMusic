package com.java.imusic.controller;


import com.alibaba.fastjson.JSONObject;
import com.java.imusic.config.PathConfig;
import com.java.imusic.domain.User;
import com.java.imusic.service.UserService;
import com.java.imusic.utils.Consts;
import com.java.imusic.utils.SecurityUtil;
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
import java.util.List;

/**
 * 前端用户控制类
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * 添加前端用户
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addUser(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id");

        String username = request.getParameter("username").trim();     //账号
        String passwordFont = request.getParameter("password").trim();     //密码
        //加密
        String password = SecurityUtil.encrypt(passwordFont);

        String sex = request.getParameter("sex").trim();               //性别
        String email = request.getParameter("email").trim();           //电子邮箱
        String birth = request.getParameter("birth").trim();           //生日
        String introduction = request.getParameter("introduction").trim();//签名
        String location = request.getParameter("location").trim();      //地区
        String profilePicture = "/img/Pic/default_avatar.jpg";          //头像地址
        String name = request.getParameter("name").trim();

        if (username == null || username.isEmpty()) {
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
        User user2 = userService.getOneUserWithName(name);

        if (user2 != null) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "昵称已被占用");
            return jsonObject;
        }
        if (passwordFont == null || passwordFont.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "密码不能为空");
            return jsonObject;
        }

        //把生日转换成Date格式
        String tmpDate = request.getParameter("birth").trim();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = new Date();
        if(!"undefined".equals(tmpDate)) {
            try {
                birthDate = dateFormat.parse(birth);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            birthDate = null;
        }

        //Integer userID = userService.lastUserID()+1;
        //Integer singerId = singerService.lastSingerID()+1;

        //保存到前端用户的对象中
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setSex(new Byte(sex));
        user.setEmail(email);
        user.setBirth(birthDate);
        user.setIntroduction(introduction);
        user.setLocation(location);
        user.setProfilePicture(profilePicture);
        user.setName(name);
        //user.setSingerID(singerId);
        //user.setId(userID);

        if (id!=null&&!id.isEmpty()){
            user.setId(Integer.parseInt(id));
        }
        System.out.println(user.getId());
        boolean flag = userService.insert(user);
        if (!flag) {   //保存成功
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "添加用户失败");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 1);
        jsonObject.put(Consts.MSG, "添加成功");
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
        String passwordFont = request.getParameter("password").trim();     //密码
        String password = SecurityUtil.encrypt(passwordFont);
        String sex = request.getParameter("sex").trim();               //性别
        String email = request.getParameter("email").trim();           //电子邮箱
        String birth = request.getParameter("birth").trim();           //生日
        String introduction = request.getParameter("introduction").trim();//签名
        String location = request.getParameter("location").trim();      //地区
        String name = request.getParameter("name").trim();             //昵称


        if (username == null || username.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "用户名不能为空");
            return jsonObject;
        }
        if (passwordFont == null || passwordFont.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "密码不能为空");
            return jsonObject;
        }
        User userOrigin = userService.getUserWithID(Integer.parseInt(id));
        String usernameOrigin = userOrigin.getUsername();
        String nameOrigin = userOrigin.getName();//原来昵称

        User user1 = userService.getByUsername(username);
        if (user1 != null && !user1.getUsername().equals(usernameOrigin)) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "用户名已存在");
            return jsonObject;
        }
        User user2 = userService.getOneUserWithName(name);

        if ((user2 != null && !user2.getName().equals(nameOrigin)) ) {
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
        User user = userService.getUserWithID(Integer.parseInt(id));
        user.setId(Integer.parseInt(id));
        user.setUsername(username);
        user.setPassword(password);
        user.setSex(new Byte(sex));
        user.setEmail(email);
        user.setBirth(birthDate);
        user.setIntroduction(introduction);
        user.setLocation(location);
        user.setName(name);
        boolean flag = userService.update(user);
        if (!flag) {   //保存成功
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "用户修改失败");
            return jsonObject;
        }


        jsonObject.put(Consts.CODE, 1);
        jsonObject.put(Consts.MSG, "修改成功");
        return jsonObject;
    }


    /**
     * 删除前端用户
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteUser(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim();          //主键
        boolean flag = userService.delete(Integer.parseInt(id));
        if(flag){
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "用户删除成功");
        }else{
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "用户删除失败");
        }

        return jsonObject;
    }

    /**
     * 根据主键查询整个对象
     */
    @RequestMapping(value = "/selectByPrimaryKey", method = RequestMethod.GET)
    public Object selectByPrimaryKey(HttpServletRequest request) {
        String id = request.getParameter("id").trim();          //主键
        User user =  userService.selectByPrimaryKey(Integer.parseInt(id));
        if(user!=null&&user.getPassword()!=null&&!user.getPassword().isEmpty()){
            user.setPassword(SecurityUtil.decrypt(user.getPassword()));
        }

        return user;
    }

    /**
     * 根据Username查询整个对象
     */
    @RequestMapping(value = "/getByUsername", method = RequestMethod.GET)
    public Object getByUsername(HttpServletRequest request) {
        String username = request.getParameter("username").trim();          //username
        User user =  userService.getByUsername(username);
        if(user==null){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Consts.MSG,"没有这名用户");
            return jsonObject;
        }
        user.setPassword(SecurityUtil.decrypt(user.getPassword()));
        return user;
    }

    /**
     * 查询所有前端用户
     */
    @RequestMapping(value = "/allUser", method = RequestMethod.GET)
    public Object allUser(HttpServletRequest request) {
        List<User> userList = userService.allUser();
        userList.forEach(user -> {
            user.setPassword(SecurityUtil.decrypt(user.getPassword()));
        });
        return userList;
    }

    /**
     * 精准获取名字对应用户
     * @param request
     * @return User
     */
    @RequestMapping(value = "/getOneUserWithName", method = RequestMethod.GET)
    public User getOneUserWithName(HttpServletRequest request){
        String name = request.getParameter("name").trim();
        return userService.getOneUserWithName(name);
    };

    /**
     * 模糊搜索名字对应用户
     * @param request
     * @return List<User>
     */
    @RequestMapping(value = "/getUserWithName", method = RequestMethod.GET)
    public List<User> getUserWithName(HttpServletRequest request){
        String name = request.getParameter("name").trim();
        return userService.getUserWithName(name);
    };

    /**
     * 获取性别对应用户
     * @param request
     * @return List<User>
     */
    @RequestMapping(value = "/getUsersWithSex", method = RequestMethod.GET)
    public List<User> getUsersWithSex(HttpServletRequest request){
        Integer sex = Integer.parseInt(request.getParameter("sex").trim());
        return userService.getUsersWithSex(sex);
    };

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
        String fileName = System.currentTimeMillis() + profilePictureFile.getOriginalFilename() ;
        //文件路径
        String filePath = PathConfig.path + System.getProperty("file.separator") + "img/Pic";
        //如果文件路径不存在，新增该路径
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }
        //实际的文件地址
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        //存储到数据库里的相对文件地址
        String storeProfilePicturePath = "/img/Pic/" + fileName;
        try {
            profilePictureFile.transferTo(dest);
            User user = userService.getUserWithID(id);
            user.setProfilePicture(storeProfilePicturePath);

            String oldPic = user.getProfilePicture();

            boolean flag = userService.update(user);
            if (!flag) {
                jsonObject.put(Consts.CODE, 0);
                jsonObject.put(Consts.MSG, "上传用户图片失败");
                return jsonObject;
            }

            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "上传成功");
            jsonObject.put("profilePicture", storeProfilePicturePath);

            if(oldPic!=null&&!oldPic.equals("/img/Pic/default_avatar.jpg")){
                File userPic = new File(filePath+System.getProperty("file.separator") + oldPic);
                if (!userPic.delete()) {
                    System.out.println(oldPic + ":\n" + "歌手头像不存在或删除失败:SingerController-deleteSinger");
                }
            }
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
    public Object login(HttpServletRequest request, HttpSession session,HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        String username = request.getParameter("username");     //账号
        String password = request.getParameter("password");
        String cypher = SecurityUtil.encrypt(password);

        //加密前端传入的 密码
        //根据用户名和密码获取数据库里面所有的信息
        boolean flag = userService.verifyPassword(username,cypher);
        //如果查到了用户
        if (!flag) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "登录失败");
            return jsonObject;
        }
        //设置登录状态
        jsonObject.put(Consts.CODE, 1);
        jsonObject.put(Consts.MSG, "登录成功");
        User user = userService.getByUsername(username);
        jsonObject.put("userId",user.getId());
        jsonObject.put("username",username);
        jsonObject.put("avatar",user.getProfilePicture());
        Cookie cookie_username = new Cookie("cookie_username",username);
        Cookie cookie_cypher = new Cookie("cookie_cypher",cypher);
        cookie_username.setMaxAge(60 * 60);//1h
        cookie_cypher.setMaxAge(60 * 60);//1h
        cookie_username.setPath(request.getContextPath());
        cookie_cypher.setPath(request.getContextPath());
        response.addCookie(cookie_username);
        response.addCookie(cookie_cypher);
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
    public void logout(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        System.out.println("===退出登录===");
        session.removeAttribute(Consts.NAME);
        Cookie cookie_cypher = new Cookie("cookie_cypher","");
        Cookie cookie_username = new Cookie("cookie_username","");
        cookie_cypher.setMaxAge(0);
        cookie_username.setMaxAge(0);
        cookie_cypher.setPath(request.getContextPath());
        cookie_username.setPath(request.getContextPath());
        response.addCookie(cookie_cypher);
        response.addCookie(cookie_username);
    }

    @RequestMapping(value = "/preLogin",method = RequestMethod.POST)
    public Object preLogin(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        String username = "";
        String cypher = "";
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie item:cookies){
                if("cookie_username".equals(item.getName())){
                    username = item.getValue();
                }
                else if("cookie_cypher".equals(item.getName())){
                    cypher = item.getValue();
                }
            }
        }
        boolean flag = userService.verifyPassword(username,cypher);
        if(!flag){
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"用户名或密码错误");
            return jsonObject;
        }
        User user = userService.getByUsername(username);
        jsonObject.put(Consts.CODE,1);
        jsonObject.put(Consts.MSG,"登录成功");
        jsonObject.put("username",username);
        jsonObject.put("userId",user.getId());
        jsonObject.put("avatar",user.getProfilePicture());
        return jsonObject;
    }

}
