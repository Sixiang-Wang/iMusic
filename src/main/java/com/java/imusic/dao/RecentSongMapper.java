package com.java.imusic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.imusic.domain.RecentSong;
import com.java.imusic.domain.Singer;
import com.java.imusic.domain.SongList;
import com.java.imusic.vo.RecentSongVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.mybatis.spring.annotation.MapperScan;

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
    @Select("select\n" +
            "            s.id,\n" +
            "            s.singer_id,\n" +
            "            s.name,\n" +
            "            s2.name,\n" +
            "            s.introduction,\n" +
            "            s.pic,\n" +
            "            s.lyric,\n" +
            "            s.url,\n" +
            "            r.count\n" +
            "        from\n" +
            "            recent_song r\n" +
            "                inner join song s on r.song_id = s.id\n" +
            "                inner join singer s2 on s.singer_id = s2.id\n" +
            "        where\n" +
            "            r.user_id = #{id}\n" +
            "        order by r.update_time desc,\n" +
            "                 r.count desc")
    List<RecentSongVo> getRecentSongByUserId(Integer id);

    /**
     * 由最近播放歌曲查询其歌单信息
     */
    @Select("select\n" +
            "            sl.id,\n" +
            "            sl.title,\n" +
            "            sl.pic,\n" +
            "            sl.introduction,\n" +
            "            sl.style\n" +
            "        from recent_song r\n" +
            "            inner join song s on r.song_id = s.id\n" +
            "            inner join list_song ls on s.id = ls.song_id\n" +
            "            inner join song_list sl on ls.song_list_id = sl.id\n" +
            "        where r.user_id = #{userId}")
    List<SongList> getSongListByRecentSong(Integer userId);

    /**
     * 由最近播放歌曲查询其歌手信息
     */
    @Select("select\n" +
            "            s2.id,\n" +
            "            s2.name,\n" +
            "            s2.sex,\n" +
            "            s2.pic,\n" +
            "            s2.birth,\n" +
            "            s2.location,\n" +
            "            s2.introduction\n" +
            "        from recent_song r\n" +
            "            inner join song s on r.song_id = s.id\n" +
            "            inner join singer s2 on s.singer_id = s2.id\n" +
            "        where r.user_id = #{userId}")
    List<Singer> getSingerByRecentSong(Integer userId);

}





