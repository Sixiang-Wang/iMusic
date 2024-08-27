package com.java.imusic.service;

import com.java.imusic.domain.Collect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收藏service接口
 */
public interface CollectService {
    /**
     *增加
     */
    public boolean insert(Collect collect);

    /**
     * 删除
     */
    public boolean delete(Integer id);

    boolean deleteBySongId(Integer songId);

    /**
     * 根据用户id和歌曲id删除
     */
    public boolean deleteByUserIdSongId(Integer userId, Integer songId);

    public boolean deleteByUserIdSongListId(Integer userId, Integer songListId);
    /**
     * 查询所有收藏
     */
    public List<Collect> allCollect();

    /**
     * 查询某个用户的收藏列表
     */
    public List<Collect> collectOfUserId(Integer userId);

    /**
     * 歌曲收藏量
     * @param songId
     * @return
     */
    public Integer songCollectNum(Integer songId);

    /**
     * 歌单收藏量
     * @param songListId
     * @return
     */
    public Integer songListCollectNum(Integer songListId);

    /**
     * 查询某个用户是否已经收藏了某个歌曲
     */
    public boolean existSongId(@Param("userId") Integer userId, @Param("songId") Integer songId);

    /**
     * 查询某个用户是否已经收藏了某个歌单
     *
     * @param userId
     * @param songListId
     */
    public boolean existSongListId(@Param("userId") Integer userId, @Param("songListId") Integer songListId);
}
