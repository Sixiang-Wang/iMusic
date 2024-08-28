package com.java.extraPart.service.impl;

import com.java.extraPart.service.RankService;
import com.java.extraPart.dao.RankMapper;
import com.java.extraPart.domain.Rank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 评价service实现
 */
@Service
public class RankServiceImpl implements RankService {

    @Autowired
    private RankMapper rankMapper;

    /**
     * 增加
     *
     * @param rank
     */
    @Override
    public boolean insert(Rank rank) {
        return rankMapper.insert(rank)>0;
    }

    @Override
    public boolean update(Rank rank){ return rankMapper.update(rank)>0; }
    /**
     * 查总分
     *
     * @param songListId
     */
    @Override
    public int selectScoreSum(Integer songListId) {
        return rankMapper.selectScoreSum(songListId);
    }

    /**
     * 查总评分人数
     *
     * @param songListId
     */
    @Override
    public int selectRankNum(Integer songListId) {
        return rankMapper.selectRankNum(songListId);
    }

    /**
     * 计算平均分
     *
     * @param songListId
     */
    @Override
    public int rankOfSongListId(Integer songListId) {
        int rankNum = rankMapper.selectRankNum(songListId);
        System.out.println(rankNum);
        if(rankNum==0){
            return -1;
        }
        return rankMapper.selectScoreSum(songListId)/rankNum;
    }

    @Override
    public Rank getRank(Rank rankOrigin){
        System.out.println(rankOrigin.getSongListId() +":" + rankOrigin.getUserId());
        return rankMapper.getRank(rankOrigin);
    }
}
