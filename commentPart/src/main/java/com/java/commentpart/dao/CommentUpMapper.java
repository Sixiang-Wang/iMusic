package com.java.commentpart.dao;

import com.java.commentpart.domain.CommentUp;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentUpMapper {
    public int insert(CommentUp commentUp);
    public int delete(@Param("userId")Integer userId, @Param("commentId")Integer commentId);
    public int deleteByCommentId(@Param("commentId")Integer commentId);
    public int exist(@Param("userId")Integer userId, @Param("commentId")Integer commentId);
}
