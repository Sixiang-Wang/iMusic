package com.java.imusic.service;


import org.apache.ibatis.annotations.Select;

/**
 * 管理员service接口
 */

public interface AdminService {

    /**
     *验证密码是否正确
     */

    public boolean verifyPassword(String username,String password);
}
