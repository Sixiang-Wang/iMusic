package com.java.imusic.dao;

import com.java.imusic.domain.CommentUp;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentUpMapper {
    public int insert(CommentUp commentUp);
    public int delete(@Param("userId")Integer userId, @Param("commentId")Integer commentId);
    public int deleteByCommentId(@Param("commentId")Integer commentId);
    public int exist(@Param("userId")Integer userId, @Param("commentId")Integer commentId);
}
