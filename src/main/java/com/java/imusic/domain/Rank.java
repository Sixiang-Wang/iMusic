package com.java.imusic.domain;

import java.io.Serializable;

/**
 * 评价
 */
public class Rank implements Serializable {
    /*主键*/
    private Integer id;
    /*歌单id*/
    private Integer songListId;
    /*用户id*/
    private Integer userId;
    /*评分*/
    private Integer score;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSongListId() {
        return songListId;
    }

    public void setSongListId(Integer songListId) {
        this.songListId = songListId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
