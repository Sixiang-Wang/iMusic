package com.java.imusic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.imusic.domain.RecentSong;
import com.java.imusic.domain.SongList;
import com.java.imusic.domain.User;
import com.java.imusic.vo.RecentSongVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * description 针对表【recent_song】的数据库操作Mapper
 * Entity com.java.imusic.domain.RecentSong
 */
@Mapper
public interface RecentSongMapper extends BaseMapper<RecentSong> {

    /**
     * 查询所有播放记录
     */
    List<RecentSong> getAllRecentSong();

    /**
     * 获取当前用户最近播放列表
     */
    @Select("select DISTINCT\n" +
            "            s.id,\n" +
            "            s.user_id,\n" +
            "            s.name,\n" +
            "            r.update_time,\n" +
            "            s.introduction,\n" +
            "            s.pic,\n" +
            "            s.lyric,\n" +
            "            s.url,\n" +
            "            r.count\n" +
            "        from\n" +
            "            recent_song r\n" +
            "                inner join song s on r.song_id = s.id\n" +
            "        where\n" +
            "            r.user_id = #{id}\n" +
            "        order by r.update_time desc,\n" +
            "                 r.count desc")
    List<RecentSongVo> getRecentSongByUserId(Integer id);


    /**
     * 获取指定用户最近播放列表，按播放量降序排列
     */
    @Select("SELECT DISTINCT\n" +
            "    s.id,\n" +
            "    s.user_id,\n" +
            "    s.name,\n" +
            "    r.update_time,\n" +
            "    s.introduction,\n" +
            "    s.pic,\n" +
            "    s.lyric,\n" +
            "    s.url,\n" +
            "    r.count\n" +
            "FROM\n" +
            "    recent_song r\n" +
            "INNER JOIN song s ON r.song_id = s.id\n" +
            "WHERE\n" +
            "    r.user_id = #{id}\n" +
            "ORDER BY r.count DESC")
    List<RecentSongVo> getRecentSongByUserIdOrderByCountDesc(Integer id);

    /**
     * 由最近播放歌曲查询其歌单信息
     */
    @Select("SELECT DISTINCT\n" +
            "            sl.id,\n" +
            "            sl.title,\n" +
            "            sl.pic,\n" +
            "            sl.introduction,\n" +
            "            sl.style\n" +
            "        FROM recent_song r\n" +
            "            INNER JOIN song s ON r.song_id = s.id\n" +
            "            INNER JOIN list_song ls ON s.id = ls.song_id\n" +
            "            INNER JOIN song_list sl ON ls.song_list_id = sl.id\n" +
            "        WHERE r.user_id = #{userId}")
    List<SongList> getSongListByRecentSong(Integer userId);

    /**
     * 由最近播放歌曲查询其歌手信息
     */
    @Select("select DISTINCT\n" +
            "            s.user_id\n" +
            "        from recent_song r\n" +
            "            inner join song s on r.song_id = s.id\n" +
            "        where r.user_id = #{userId}")
    List<Integer> getSingerByRecentSong(Integer userId);

}





