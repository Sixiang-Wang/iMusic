package com.java.commentpart.service;

import com.java.commentpart.domain.CommentUp;

public interface CommentUpService {
    public boolean insert(CommentUp commentUp);
    public boolean delete(Integer userId,Integer commentId);
    public boolean exist(Integer userId,Integer commentId);
}
