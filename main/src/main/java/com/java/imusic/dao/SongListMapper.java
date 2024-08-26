package com.java.imusic.dao;

import com.java.imusic.domain.Song;
import com.java.imusic.domain.SongList;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 歌单Dao
 */
@Repository
public interface SongListMapper {
    /**
     *增加
     */
    public int insert(SongList songList);

    /**
     *修改
     */
    public int update(SongList songList);

    /**
     * 删除
     */
    public int delete(Integer id);

    /**
     * 根据主键查询整个对象
     */
    public SongList selectByPrimaryKey(Integer id);

    /**
     * 查询所有歌单
     */
    public List<SongList> allSongList();

    /**
     * 根据标题精确查询歌单列表
     */
    public List<SongList> songListOfTitle(String title);

    /**
     * 根据标题模糊查询歌单列表
     */
    public List<SongList> likeTitle(String title);

    /**
     * 根据风格模糊查询歌单列表
     */
    public List<SongList> likeStyle(String style);

    public List<SongList> likeOtherStyle();

    /**
     * 根据userId查询整个对象
     */
    public List<SongList> selectByUserId(Integer userId);

    public List<SongList> allInvisible();

}
















