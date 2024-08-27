package com.java.songPart.dao;

import com.java.songPart.domain.ListSong;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 歌单里面的歌曲Dao
 */
@Repository
public interface ListSongMapper {
    /**
     * 增加
     */
    public int insert(ListSong listSong);

    /**
     * 修改
     */
    public int update(ListSong listSong);

    /**
     * 删除
     */
    public int delete(Integer id);

    /**
     * 根据歌曲id和歌单id删除
     */
    public int deleteBySongIdAndSongListId(@Param("songId") Integer songId, @Param("songListId") Integer songListId);

    public int deleteBySongListId(@Param("songListId") Integer songListId);
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
    int updVipStatusById(@Param("id") String id, @Param("isVip") Boolean isVip);
}
















