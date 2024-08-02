package com.java.imusic.service.impl;

import com.java.imusic.config.PathConfig;
import com.java.imusic.dao.CollectMapper;
import com.java.imusic.dao.ComplaintMapper;
import com.java.imusic.dao.SongMapper;
import com.java.imusic.domain.Song;
import com.java.imusic.service.CommentService;
import com.java.imusic.service.SongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * 歌曲service实现类
 */
@Service
@Slf4j
public class SongServiceImpl implements SongService {
    @Autowired
    private SongMapper songMapper;
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private ComplaintMapper complaintMapper;
    @Autowired
    private CommentService commentService;

    /**
     * 增加
     *
     * @param song
     */
    @Override
    public boolean insert(Song song) {
        return songMapper.insert(song) > 0;
    }

    /**
     * 修改
     *
     * @param song
     */
    @Override
    public boolean update(Song song) {
        return songMapper.update(song) > 0;
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public boolean delete(Integer id) {
        Song song = songMapper.selectByPrimaryKey(id);
        String songUrl = song.getUrl();
        String picUrl = song.getPic();
        File songFile = new File(PathConfig.path +System.getProperty("file.separator")+songUrl);
        File picFile = new File(PathConfig.path +System.getProperty("file.separator")+picUrl);

        collectMapper.deleteBySongId(song.getId());
        complaintMapper.deleteBySongId(song.getId());
        songMapper.deleteRecentSong(song.getId());
        commentService.deleteAllOfSong(song.getId());

        if(!songFile.delete()) {
            System.out.println("歌曲源删除失败:SongController-deleteSong");
        }
        if(!picUrl.equals("/img/songPic/default.jpg")){
            if(!picFile.delete()) {
                System.out.println("歌曲图片删除失败:SongController-deleteSong");
            }
        }
        return songMapper.delete(id) > 0;
    }

    /**
     * 根据主键查询整个对象
     *
     * @param id
     */
    @Override
    public Song selectByPrimaryKey(Integer id) {
        Song song = songMapper.selectByPrimaryKey(id);
        return songMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有歌曲
     */
    @Override
    public List<Song> allSong() {
        return songMapper.allSong();
    }

    /**
     * 根据歌名精确查询列表
     *
     * @param name
     */
    @Override
    public List<Song> songOfName(String name) {
        return songMapper.songOfName(name);
    }

    /**
     * 根据歌名模糊查询列表
     *
     * @param name
     */
    @Override
    public List<Song> likeSongOfName(String name) {
        //log.error(">>>>>>>>>>>>>搜索的名字{}", name);

        List<Song> songs = songMapper.likeSongOfName("%" + name + "%");
        //songs.forEach(song -> log.warn(">>>>>>>>>>>>>>{}", song.getIsVip()));
        return songs;
    }


    /**
     * 根据歌手id查询
     *
     * @param userId
     */
    @Override
    public List<Song> songOfUserId(Integer userId) {
        return songMapper.songOfUserId(userId);
    }

    @Override
    public boolean addNums(Integer id) {
        return songMapper.addNums(id);
    }

    @Override
    public List<Song> topSong() {
        return songMapper.topSong();
    }

    @Override
    public List<Song> songOfStyle(String style){
        return songMapper.songOfStyle(style);
    };
}
