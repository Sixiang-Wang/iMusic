package com.java.imusic.service;

import com.java.imusic.domain.Comment;
import com.java.imusic.domain.CommentUp;

public interface CommentUpService {
    public boolean insert(CommentUp commentUp);
    public boolean delete(Integer userId,Integer commentId);
    public boolean exist(Integer userId,Integer commentId);
}
