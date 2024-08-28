package com.java.songPart.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.java.songPart.config.PathConfig;
import com.java.songPart.dao.ListSongMapper;
import com.java.songPart.dao.SongListMapper;
import com.java.songPart.domain.SongList;
import com.java.songPart.service.SongListService;
import com.java.songPart.utils.Port;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.List;

/**
 * 歌单service实现类
 */
@Service
public class SongListServiceImpl implements SongListService {

    @Autowired
    private SongListMapper songListMapper;
    @Autowired
    private ListSongMapper listSongMapper;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 增加
     *
     * @param songList
     */
    @Override
    public boolean insert(SongList songList) {
        return songListMapper.insert(songList)>0;
    }

    /**
     * 修改
     *
     * @param songList
     */
    @Override
    public boolean update(SongList songList) {
        return songListMapper.update(songList)>0;
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public boolean delete(Integer id) {
        SongList songList = songListMapper.selectByPrimaryKey(id);
        String oldPic = songList.getPic();
        File oldPicFile = new File(PathConfig.path +System.getProperty("file.separator")+oldPic);
        if(!oldPic.equals("/img/songListPic/default.jpg")){
            if(!oldPicFile.delete()) {
                System.out.println("删除歌单图片失败-deleteSongList");
            }
        }

//        collectMapper.deleteBySongListId(songList.getId());
        restTemplate.getForObject(
                Port.url_base+Port.port_extra+"/deleteBySongListId?songListId="+songList.getId(),
                Boolean.class
        );
        listSongMapper.deleteBySongListId(songList.getId());
        restTemplate.getForObject(
                Port.url_base+Port.port_extra+"/deleteBySongListId?songListId="+songList.getId(),
                Boolean.class
        );
//        complaintMapper.deleteBySongId(songList.getId());
//        commentService.deleteAllOfSongList(songList.getId());
        restTemplate.getForObject(
                Port.url_base+Port.port_comment+"/deleteBySongListId?songListId="+songList.getId(),
                Boolean.class
        );
        return songListMapper.delete(id)>0;
    }

    /**
     * 根据主键查询整个对象
     *
     * @param id
     */
    @Override
    public SongList selectByPrimaryKey(Integer id) {
        return songListMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有歌单
     */
    @Override
    public List<SongList> allSongList() {
        return songListMapper.allSongList();
    }

    /**
     * 根据标题精确查询歌单列表
     *
     * @param title
     */
    @Override
    public List<SongList> songListOfTitle(String title) {
        return songListMapper.songListOfTitle(title);
    }

    /**
     * 根据标题模糊查询歌单列表
     *
     * @param title
     */
    @Override
    public List<SongList> likeTitle(String title) {
        return songListMapper.likeTitle(title);
    }

    /**
     * 根据风格模糊查询歌单列表
     *
     * @param style
     */
    @Override
    public List<SongList> likeStyle(String style) {
        return songListMapper.likeStyle(style);
    }

    /**
     * 根据userId查询整个对象
     */
    @Override
    public List<SongList> selectByUserId(Integer userId){
        return songListMapper.selectByUserId(userId);
    }
}
