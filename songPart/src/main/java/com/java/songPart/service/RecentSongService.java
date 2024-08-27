package com.java.songPart.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.java.songPart.common.Result;
import com.java.songPart.domain.RecentSong;

/**
 * description 针对表【recent_song】的数据库操作Service
 */
public interface RecentSongService extends IService<RecentSong> {

    /**
     * 添加歌曲最近播放记录
     */
    Result addRecentSong(RecentSong recentSong);

    /**
     * 获取当前用户最近播放列表
     */
    Result getRecentSongByUserId(Integer id);

    /**
     * 获取指定用户最近播放列表，按播放量降序排列
     */
    Result getRecentSongByUserIdOrderByCountDesc(Integer id);

    /**
     * 推荐歌单
     */
    Result recommendSongList(Integer id);

    /**
     * 推荐歌手
     */
    Result recommendSinger(Integer id);
}
