package com.java.imusic.service.impl;

import com.java.imusic.dao.FollowMapper;
import com.java.imusic.domain.Comment;
import com.java.imusic.domain.Follow;
import com.java.imusic.domain.Follow;
import com.java.imusic.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    private FollowMapper followMapper;
    /**
     * 增加
     *
     * @param follow
     */
    @Override
    public int insert(Follow follow) {
        return followMapper.insert(follow);
    }

    /**
     * 修改
     *
     * @param follow
     */
    @Override
    public int update(Follow follow) {
        return followMapper.update(follow);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public int delete(Integer id) {
        return followMapper.delete(id);
    }


    /**
     * 根据主键查询整个对象
     *
     * @param id
     */
    @Override
    public Follow selectByPrimaryKey(Integer id) {
        return followMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Follow> getByUserId(Integer userId) {
        return followMapper.getByUserId(userId);
    }

    @Override
    public List<Follow> getBySingerId(Integer singerId) {
        return followMapper.getBySingerId(singerId);
    }

    @Override
    public Integer getCountByUserId(Integer userId) {
        return followMapper.getCountByUserId(userId);
    }

    @Override
    public Integer getCountBySingerId(Integer singerId) {
        return followMapper.getCountBySingerId(singerId);
    }


}
