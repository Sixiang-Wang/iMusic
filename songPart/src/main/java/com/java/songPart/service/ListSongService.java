package com.java.songPart.service;

import com.java.songPart.domain.ListSong;

import java.util.List;

/**
 * 歌单里面的歌曲service接口
 */
public interface ListSongService {
    /**
     * 增加
     */
    public boolean insert(ListSong listSong);

    /**
     * 修改
     */
    public boolean update(ListSong listSong);

    /**
     * 删除
     */
    public boolean delete(Integer id);

    /**
     * 根据歌曲id和歌单id删除
     */
    public boolean deleteBySongIdAndSongListId(Integer songId, Integer songListId);

    /**
     * 根据主键查询整个对象
     */
    public ListSong selectByPrimaryKey(Integer id);

    /**
     * 查询所有歌单里面的歌曲
     */
    public List<ListSong> allListSong();

    /**
     * 根据歌单id查询所有的歌曲
     */
    public List<ListSong> listSongOfSongListId(Integer songListId);

    /**
     * 根据 id 修改歌曲vip状态
     *
     * @param id:
     * @return int
     * @since 2023/3/3 10:59
     */
    int updVipStatusById(String id, Boolean isVip);
}
