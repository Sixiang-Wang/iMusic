package com.java.imusic.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Admin
 */
public class Admin implements Serializable {
    private Integer id;
    private String name;
    private String password;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



}
