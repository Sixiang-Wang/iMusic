package com.java.commentpart.service.impl;

import com.java.commentpart.dao.CommentMapper;
import com.java.commentpart.dao.CommentUpMapper;
import com.java.commentpart.domain.Comment;
import com.java.commentpart.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论service实现类
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentUpMapper commentUpMapper;
    /**
     * 增加
     *
     * @param comment
     */
    @Override
    public boolean insert(Comment comment) {
        return commentMapper.insert(comment)>0;
    }

    /**
     * 修改
     *
     * @param comment
     */
    @Override
    public boolean update(Comment comment) {
        return commentMapper.update(comment)>0;
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public boolean delete(Integer id) {
        commentUpMapper.deleteByCommentId(id);
        return commentMapper.delete(id)>0;
    }

    @Override
    public boolean deleteAllOfSong(Integer songId){
        List<Comment> commentList = commentMapper.commentOfSongId(songId);
        commentList.forEach(comment -> {
            commentUpMapper.deleteByCommentId(comment.getId());
        });
        return commentMapper.deleteAllOfSong(songId) > 0;
    }


    @Override
    public boolean deleteAllOfSongList(Integer songListId){
        List<Comment> commentList = commentMapper.commentOfSongListId(songListId);
        commentList.forEach(comment -> {
            commentUpMapper.deleteByCommentId(comment.getId());
        });
        return commentMapper.deleteAllOfSongList(songListId) > 0;
    }

    @Override
    public boolean deleteAllOfUser(Integer userId){
        List<Comment> commentList = commentMapper.commentOfUserId(userId);
        commentList.forEach(comment -> {
            commentUpMapper.deleteByCommentId(comment.getId());
        });
        return commentMapper.deleteAllOfUser(userId)>0;
    }
    /**
     * 根据主键查询整个对象
     *
     * @param id
     */
    @Override
    public Comment selectByPrimaryKey(Integer id) {
        return commentMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有评论
     */
    @Override
    public List<Comment> allComment() {
        return commentMapper.allComment();
    }

    /**
     * 查询某个歌曲下的所有评论
     *
     * @param songId
     */
    @Override
    public List<Comment> commentOfSongId(Integer songId) {
        return commentMapper.commentOfSongId(songId);
    }

    /**
     * 查询某个歌单下的所有评论
     *
     * @param songListId
     */
    @Override
    public List<Comment> commentOfSongListId(Integer songListId) {
        return commentMapper.commentOfSongListId(songListId);
    }
}
