package com.java.songPart.service.impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.songPart.config.PathConfig;
import com.java.songPart.dao.SongMapper;
import com.java.songPart.domain.Song;
import com.java.songPart.domain.User;
import com.java.songPart.service.SongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.List;

/**
 * 歌曲service实现类
 */
@Service
@Slf4j
public class SongServiceImpl implements SongService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SongMapper songMapper;

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

//        collectMapper.deleteBySongId(song.getId());
        restTemplate.exchange(
                "http://101.201.173.110:1145/collect/deleteBySongId?songId="+song.getId(),
                HttpMethod.GET,
                null,
                Boolean.class
        );


//        complaintMapper.deleteBySongId(song.getId());
        restTemplate.exchange(
                "http://101.201.173.110:1145/complaint/deleteBySongId?songId="+song.getId(),
                HttpMethod.GET,
                null,
                Boolean.class
        );
        songMapper.deleteRecentSong(song.getId());

//        commentService.deleteAllOfSong(song.getId());
        restTemplate.exchange(
                "http://101.201.173.110:1145/comment/deleteBySongId?songId="+song.getId(),
                HttpMethod.GET,
                null,
                Boolean.class
        );

        if(!songFile.delete()) {
            System.out.println("歌曲源删除失败:SongController-deleteSong");
        }
        if(picUrl!=null&&!picUrl.equals("/img/songPic/default.jpg")){
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

    /**
     * 增加歌曲名字前面的用户名前缀
     * @param song
     * @return
     */
    @Override
    public Song addPrefix(Song song){
        String url = "http://101.201.173.110:1145/user/selectByPrimaryKey?id="+song.getUserId();
        User user = restTemplate.getForObject(url, User.class);
        if (user != null)
        {
            String name = user.getName() + "-" + song.getName();
            song.setName(name);
        }
//        song.setName(userMapper.getUserWithID(song.getUserId()).getName() + "-" + song.getName());
        return song;
    }

    /**
     * 增加列表中歌曲名字前面的用户名前缀
     * @param songs
     * @return
     */
    @Override
    public List<Song> addPrefix(List<Song> songs){
        songs.forEach(song -> {
            String url = "http://101.201.173.110:1145/user/selectByPrimaryKey?id="+song.getUserId();
            User user = restTemplate.getForObject(url, User.class);
            if (user != null)
            {
                String name = user.getName() + "-" + song.getName();
                song.setName(name);
            }
//            song.setName(userMapper.getUserWithID(song.getUserId()).getName() + "-" + song.getName());
        });
        return songs;
    }
}
