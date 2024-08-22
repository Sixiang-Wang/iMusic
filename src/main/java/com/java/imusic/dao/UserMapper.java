package com.java.imusic.dao;

import com.java.imusic.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 前端用户Dao
 */
@Repository
public interface UserMapper {
    /**
     * 增加
     */
    public int insert(User user);

    /**
     * 修改
     */
    public int update(User user);

    /**
     * 删除
     */
    public int delete(Integer id);

    /**
     * 根据主键查询整个对象
     */
    public User selectByPrimaryKey(Integer id);

    /**
     * 查询所有用户
     */
    public List<User> allUser();

    /**
     * 验证密码
     */
    public int verifyPassword(@Param("username") String username, @Param("password") String password);

    /**
     * 根据账号查询
     */
    public User getByUsername(@Param("username") String username);

    /**
     * 使用帐号密码登录
     *
     * @param username: 帐号
     * @param password: 密码
     * @return com.java.imusic.domain.User
     * @since 2023/3/3 13:49
     */
    User getUserWithAccount(@Param("username") String username, @Param("password") String password);

    /**
     * 第三方登录  使用手机号码
     *
     * @param phoneNum :
     * @return com.java.imusic.domain.User
     * @since 2023/3/3 14:23
     */
    User getUserWithPhoneNum(@Param("phoneNum") String phoneNum);

    /**
     * 获得用户的“名字”对应用户
     *
     * @param name:  用户id
     * @return com.java.imusic.domain.User
     * @since 2023/3/3 22:47
     */
    User getOneUserWithName(@Param("name") String name);


    public List<User> getUserWithName(@Param("name") String name);

    public List<User> getUsersWithSex(Integer sex);

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
















