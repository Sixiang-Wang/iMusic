package com.java.imusic.dao;

import com.java.imusic.domain.Song;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 歌手Dao
 */
@Repository
public interface SongMapper {
    /**
     *增加
     */
    public int insert(Song song);

    /**
     *修改
     */
    public int update(Song song);

    /**
     * 删除
     */
    public int delete(Integer id);

    public int deleteRecentSong(Integer songId);

    /**
     * 根据主键查询整个对象
     */
    public Song selectByPrimaryKey(Integer id);

    /**
     * 查询所有歌曲
     */
    public List<Song> allSong();

    public List<Song> allInvisible();

    /**
     * 根据歌名精确查询列表
     */
    public List<Song> songOfName(String name);

    /**
     * 根据歌名模糊查询列表
     */
    public List<Song> likeSongOfName(String name);

    /**
     * 根据用户id查询
     */
    public List<Song> songOfUserId(Integer userId);

    /**
     * 增加歌曲播放次数
     */
	public boolean addNums(Integer id);

	/**
     * 查询播放次数排前列的歌曲
     */
	public List<Song> topSong();

    public List<Song> songOfStyle(String style);
    public List<Song> songOfOtherStyle();

    public Song popularSongOfUser(Integer userId);

    public Song popularCollectedSongOfUser(Integer userId);
}
















