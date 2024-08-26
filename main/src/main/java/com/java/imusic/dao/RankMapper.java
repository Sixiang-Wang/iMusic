package com.java.imusic.dao;

import com.java.imusic.domain.Rank;
import com.java.imusic.domain.SongList;
import org.springframework.stereotype.Repository;

/**
 * 评价Dao
 */
@Repository
public interface RankMapper {
    /**
     *增加
     */
    public int insert(Rank rank);

    public int update(Rank rank);
    /**
     * 查总分
     */
    public int selectScoreSum(Integer songListId);

    /**
     * 查总评分人数
     */
    public int selectRankNum(Integer songListId);

    public Rank getRank(Rank rank);

    public SongList bestSongListOfUser(Integer userId);
}
















