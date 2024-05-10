package com.java.imusic.domain;

import lombok.Data;

import java.util.Date;

/**
 * 前端用户
 */
@Data
public class User {
    /*主键*/
    private Integer id;
    /*账号*/
    private String username;
    /*密码*/
    private String password;
    /*性别*/
    private Byte sex;
    /*手机号*/
    private String phoneNum;
    /*电子邮箱*/
    private String email;
    /*生日*/
    private Date birth;
    /*签名*/
    private String introduction;
    /*地区*/
    private String location;
    /*头像*/
    private String profilePicture;
    /*创建时间*/
    private Date createTime;
    /*更新时间*/
    private Date updateTime;
    /*用户昵称*/
    private String name;
    private Integer singerID;
}
