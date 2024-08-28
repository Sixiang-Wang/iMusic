package com.java.userpart.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Admin Dao
 */
@Repository
public interface AdminMapper {
    /**
     * 验证密码
     */
    public int verifyPassword(String username,String password);
}
