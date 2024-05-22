package com.java.imusic.service;

import com.java.imusic.domain.Follow;

import java.util.List;

public interface FollowService {
    /**
     * 增加
     */
    public int insert(Follow follow);

    /**
     * 修改
     */
    public int update(Follow follow);

    /**
     * 删除
     */
    public int delete(Integer id);


    /**
     * 根据主键查询整个对象
     */
    public Follow selectByPrimaryKey(Integer id);

    public Follow selectByUserIdAndSingerId(Integer userId,Integer singerId);

    /**
     * 查用户关注的人
     * @param id
     * @return
     */
    public List<Follow> getByUserId(Integer id);

    /**
     * 查歌手粉丝
     * @param id
     * @return
     */
    public List<Follow> getBySingerId(Integer id);

    /**
     * 查用户关注数量
     * @param id
     * @return
     */
    public Integer getCountByUserId(Integer id);

    /**
     * 查粉丝数量
     * @param id
     * @return
     */
    public Integer getCountBySingerId(Integer id);
}
