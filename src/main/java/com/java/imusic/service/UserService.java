package com.java.imusic.service;

import com.java.imusic.domain.User;

import java.util.List;

/**
 * 前端用户service接口
 */
public interface UserService {
    /**
     * 增加
     */
    public boolean insert(User user);

    /**
     * 修改
     */
    public boolean update(User user);

    /**
     * 删除
     */
    public boolean delete(Integer id);

    /**
     * 根据主键查询整个对象
     */
    public User selectByPrimaryKey(Integer id);

    /**
     * 查询所有用户
     */
    public List<User> allUser();

    /**
     * 查看密码是否正确
     */
    public boolean verifyPassword(String username, String password);

    /**
     * 根据账号查询
     */
    public User getByUsername(String username);

    /**
     * 使用帐号密码登录
     *
     * @param username: 帐号
     * @param password: 密码
     * @return com.java.imusic.domain.User
     */
    User getUserWithAccount(String username, String password);

    /**
     * 第三方登录  使用手机号码
     *
     * @param phoneNum:
     * @return com.java.imusic.domain.User
     */
    User getUserWithPhoneNum(String phoneNum);

    /**
     * 获得用户的“名字”对应用户
     *
     * @param name:  用户id
     * @return com.java.imusic.domain.User
     */
    User getUserWithName(String name);

    /**
     * 找到用户ID最大值
     * @return Integer
     */
    public Integer lastUserID();

    /**
     * 通过ID找到用户
     * @param id
     * @return User
     */
    User getUserWithID(Integer id);
}
