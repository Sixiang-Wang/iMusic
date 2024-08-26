package com.java.imusic.service.impl;

import com.java.imusic.dao.CollectMapper;
import com.java.imusic.domain.Collect;
import com.java.imusic.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收藏service实现类
 */
@Service
public class CollectServiceImpl implements CollectService {

    @Autowired
    private CollectMapper collectMapper;


    /**
     * 增加
     *
     * @param collect
     */
    @Override
    public boolean insert(Collect collect) {
        return collectMapper.insert(collect)>0;
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public boolean delete(Integer id) {
        return collectMapper.delete(id)>0;
    }

    /**
     * 根据用户id和歌曲id删除
     *
     * @param userId
     * @param songId
     */
    @Override
    public boolean deleteByUserIdSongId(Integer userId, Integer songId) {
        return collectMapper.deleteByUserIdSongId(userId,songId)>0;
    }

    @Override
    public boolean deleteByUserIdSongListId(Integer userId, Integer songListId) {
        return collectMapper.deleteByUserIdSongListId(userId,songListId)>0;
    }

    /**
     * 查询所有收藏
     */
    @Override
    public List<Collect> allCollect() {
        return collectMapper.allCollect();
    }

    /**
     * 查询某个用户的收藏列表
     *
     * @param userId
     */
    @Override
    public List<Collect> collectOfUserId(Integer userId) {
        return collectMapper.collectOfUserId(userId);
    }

    /**
     * 歌曲收藏量
     * @param songId
     * @return
     */
    @Override
    public Integer songCollectNum(Integer songId){
        return collectMapper.songCollectNum(songId);
    }

    /**
     * 歌单收藏量
     * @param songListId
     * @return
     */
    @Override
    public Integer songListCollectNum(Integer songListId){
        return collectMapper.songListCollectNum(songListId);
    }

    /**
     * 查询某个用户是否已经收藏了某个歌曲
     *
     * @param userId
     * @param songId
     */
    @Override
    public boolean existSongId(Integer userId, Integer songId) {
        return collectMapper.existSongId(userId,songId)>0;
    }

    /**
     * 查询某个用户是否已经收藏了某个歌单
     *
     * @param userId
     * @param songListId
     */
    @Override
    public boolean existSongListId(Integer userId, Integer songListId) {
        return collectMapper.existSongListId(userId,songListId)>0;
    }
}
