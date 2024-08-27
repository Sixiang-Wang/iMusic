package com.java.commentpart.service;

import com.java.commentpart.domain.Comment;

import java.util.List;

/**
 * 评论service接口
 */
public interface CommentService {
    /**
     *增加
     */
    public boolean insert(Comment comment);

    /**
     *修改
     */
    public boolean update(Comment comment);

    /**
     * 删除
     */
    public boolean delete(Integer id);

    public boolean deleteAllOfUser(Integer userId);

    public boolean deleteAllOfSong(Integer songId);



    public boolean deleteAllOfSongList(Integer songListId);

    /**
     * 根据主键查询整个对象
     */
    public Comment selectByPrimaryKey(Integer id);

    /**
     * 查询所有评论
     */
    public List<Comment> allComment();

    /**
     * 查询某个歌曲下的所有评论
     */
    public List<Comment> commentOfSongId(Integer songId);

    /**
     * 查询某个歌单下的所有评论
     */
    public List<Comment> commentOfSongListId(Integer songListId);

    public Boolean deleteBySongId(int i);
}
