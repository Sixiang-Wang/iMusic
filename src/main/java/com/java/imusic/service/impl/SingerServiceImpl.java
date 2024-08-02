package com.java.imusic.service.impl;

import com.java.imusic.config.PathConfig;
import com.java.imusic.dao.FollowMapper;
import com.java.imusic.dao.SingerMapper;
import com.java.imusic.domain.Singer;
import com.java.imusic.domain.Song;
import com.java.imusic.service.SingerService;
import com.java.imusic.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * 歌手service实现类
 */
@Service
public class SingerServiceImpl implements SingerService {

    @Autowired
    private SingerMapper singerMapper;
    @Autowired
    private SongService songService;
    @Autowired
    private FollowMapper followMapper;

    /**
     * 增加
     *
     * @param singer
     */
    @Override
    public boolean insert(Singer singer) {
        return singerMapper.insert(singer)>0;
    }

    /**
     * 修改
     *
     * @param singer
     */
    @Override
    public boolean update(Singer singer) {
        return singerMapper.update(singer)>0;
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public boolean delete(Integer id) {
        Singer singer = singerMapper.selectByPrimaryKey(id);
        String singerPicUrl = singer.getPic();
        if(!singerPicUrl.equals("/img/Pic/default_avatar.jpg")) {
            File singerPic = new File(PathConfig.path +System.getProperty("file.separator") + singerPicUrl);
            if (!singerPic.delete()) {
                System.out.println(singerPicUrl + ":\n" + "歌手头像不存在或删除失败:SingerController-deleteSinger");
            }
        }
        List<Song> songs = songService.songOfUserId(id);
        for (Song song : songs){
            songService.delete(song.getId());
        }
        followMapper.deleteBySingerId(id);

        int ret = singerMapper.delete(id);
        return ret>0;
    }

    /**
     * 根据主键查询整个对象
     *
     * @param id
     */
    @Override
    public Singer selectByPrimaryKey(Integer id) {
        return singerMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有歌手
     */
    @Override
    public List<Singer> allSinger() {
        return singerMapper.allSinger();
    }

    /**
     * 根据歌手名字模糊查询列表
     *
     * @param name
     */
    @Override
    public List<Singer> singerOfName(String name) {
        return singerMapper.singerOfName(name);
    }

    /**
     * 根据性别查询
     *
     * @param sex
     */
    @Override
    public List<Singer> singerOfSex(Integer sex) {
        return singerMapper.singerOfSex(sex);
    }

    @Override
    public Singer oneSingerOfName(String name){
        return singerMapper.oneSingerOfName(name);
    };

    @Override
    public Integer lastSingerID(){return singerMapper.lastSingerID(); };
}
