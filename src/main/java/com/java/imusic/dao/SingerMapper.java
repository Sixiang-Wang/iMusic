package com.java.imusic.dao;

import com.java.imusic.domain.Singer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 歌手Dao
 */
@Repository
public interface SingerMapper {
    /**
     *增加
     */
    public int insert(Singer singer);

    /**
     *修改
     */
    public int update(Singer singer);

    /**
     * 删除
     */
    public int delete(Integer id);

    /**
     * 用于防止在删除大量歌手后，自增编号不连续
     */
    @Select("alter table singer auto_increment=1;")
    public void updateAutoIncrement();
    /**
     * 根据主键查询整个对象
     */
    public Singer selectByPrimaryKey(Integer id);

    /**
     * 查询所有歌手
     */
    public List<Singer> allSinger();

    /**
     * 根据歌手名字模糊查询列表
     */
    public List<Singer> singerOfName(String name);

    public Singer oneSingerOfName(String name);
    /**
     * 根据性别查询
     */
    public List<Singer> singerOfSex(Integer sex);

    public Integer lastSingerID();
}
















