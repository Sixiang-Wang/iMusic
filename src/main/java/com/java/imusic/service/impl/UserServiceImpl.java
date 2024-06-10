package com.java.imusic.service.impl;

import com.java.imusic.dao.FollowMapper;
import com.java.imusic.dao.UserMapper;
import com.java.imusic.domain.Singer;
import com.java.imusic.domain.Song;
import com.java.imusic.domain.User;
import com.java.imusic.service.CommentService;
import com.java.imusic.service.SingerService;
import com.java.imusic.service.UserService;
import com.java.imusic.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * 前端用户service实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SingerService singerService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private FollowMapper followMapper;
    /**
     * 增加
     *
     * @param user
     */
    @Override
    public boolean insert(User user) {
        return userMapper.insert(user) > 0;
    }

    /**
     * 修改
     *
     * @param user
     */
    @Override
    public boolean update(User user) {
        return userMapper.update(user) > 0;
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public boolean delete(Integer id) {
        User user = userMapper.getUserWithID(id);
        singerService.delete(user.getSingerId());
        commentService.deleteAllOfUser(user.getId());
        followMapper.deleteByUserId(user.getId());
        return userMapper.delete(id) > 0;
    }

    /**
     * 根据主键查询整个对象
     *
     * @param id
     */
    @Override
    public User selectByPrimaryKey(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有用户
     */
    @Override
    public List<User> allUser() {
        return userMapper.allUser();
    }

    /**
     * 查看密码是否正确
     *
     * @param username
     * @param password
     */
    @Override
    public boolean verifyPassword(String username, String password) {
        return userMapper.verifyPassword(username, password) > 0;
    }

    /**
     * 根据账号查询
     *
     * @param username
     */
    @Override
    public User getByUsername(String username) {
        return userMapper.getByUsername(username);
    }

    /**
     * 使用帐号密码登录
     *
     * @param username : 帐号
     * @param password : 密码
     * @return com.java.imusic.domain.User
     * @since 2023/3/3 13:49
     */
    @Override
    public User getUserWithAccount(String username, String password) {
        return userMapper.getUserWithAccount(username, password);
    }

    /**
     * 第三方登录  使用手机号码
     *
     * @param phoneNum :
     * @return com.java.imusic.domain.User
     * @since 2023/3/3 14:23
     */
    @Override
    public User getUserWithPhoneNum(String phoneNum) {
        return userMapper.getUserWithPhoneNum(phoneNum);
    }

    /**
     * 获得用户的“名字”对应用户
     *
     * @param name:  用户id
     * @return com.java.imusic.domain.User
     * @since 2023/3/3 22:47
     */
    @Override
    public User getUserWithName(String name){
        return userMapper.getUserWithName(name);
    };


    /**
     * 找到用户ID最大值
     * @return Integer
     */
    public Integer lastUserID(){return userMapper.lastUserID(); };

    /**
     * 通过ID找到用户
     * @param id
     * @return User
     */
    public User getUserWithID(Integer id){
        return userMapper.getUserWithID(id);
    };
}
