package com.java.commentpart.service.impl;

import com.java.commentpart.dao.CommentUpMapper;
import com.java.commentpart.domain.CommentUp;
import com.java.commentpart.service.CommentUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentUpServiceImpl implements CommentUpService {
    @Autowired
    private CommentUpMapper commentUpMapper;
    @Override
    public boolean insert(CommentUp commentUp) {
        return commentUpMapper.insert(commentUp) > 0;
    }

    @Override
    public boolean delete(Integer userId, Integer commentId) {
        return commentUpMapper.delete(userId,commentId) > 0;
    }

    @Override
    public boolean exist(Integer userId, Integer commentId) {
        return commentUpMapper.exist(userId,commentId) > 0;
    }
}
