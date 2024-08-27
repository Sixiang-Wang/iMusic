package com.java.songPart.service;



import com.java.songPart.domain.Song;

import java.util.List;

/**
 * 歌曲service接口
 */
public interface SongService {
    /**
     *增加
     */
    public boolean insert(Song song);

    /**
     *修改
     */
    public boolean update(Song song);


    /**
     * 删除
     */
    public boolean delete(Integer id);

    /**
     * 根据主键查询整个对象
     */
    public Song selectByPrimaryKey(Integer id);

    /**
     *增加播放次数
     */
    public boolean addNums(Integer id);

    /**
     * 查询所有歌曲
     */
    public List<Song> allSong();

    /**
     * 根据歌名精确查询列表
     */
    public List<Song> songOfName(String name);

    /**
     * 根据歌名模糊查询列表
     */
    public List<Song> likeSongOfName(String name);

    /**
     * 根据歌手id查询
     */
    public List<Song> songOfUserId(Integer singerId);

    /**
     * 查询播放次数排前列的歌曲
     */
	public List<Song> topSong();

    public List<Song> songOfStyle(String style);

    /**
     * 增加歌曲名字前面的用户名前缀
     * @param song
     * @return
     */
    public Song addPrefix(Song song);

    /**
     * 增加列表中歌曲名字前面的用户名前缀
     * @param songs
     * @return
     */
    public List<Song> addPrefix(List<Song> songs);
}
