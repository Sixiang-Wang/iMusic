package com.java.imusic.service;

import com.java.imusic.common.Result;
import com.java.imusic.domain.RecentSong;
import com.baomidou.mybatisplus.extension.service.IService;

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
     * 推荐歌单
     */
    Result recommendSongList(Integer id);

    /**
     * 推荐歌手
     */
    Result recommendSinger(Integer id);
}
