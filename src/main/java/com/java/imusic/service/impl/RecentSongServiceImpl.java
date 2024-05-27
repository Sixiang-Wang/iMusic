package com.java.imusic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.imusic.common.Result;
import com.java.imusic.domain.*;
import com.java.imusic.dao.*;
import com.java.imusic.service.CollectService;
import com.java.imusic.service.RecentSongService;
import com.java.imusic.service.SongListService;
import com.java.imusic.service.SongService;
import com.java.imusic.vo.RecentSongVo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.rmi.CORBA.Util;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Date;

/**
 * @author xs
 * description 针对表【recent_song】的数据库操作Service实现
 * createDate 2022-10-30 10:18:56
 */
@Service
public class RecentSongServiceImpl extends ServiceImpl<RecentSongMapper, RecentSong> implements RecentSongService {

    @Resource
    private RecentSongMapper recentSongMapper;

    @Resource
    private SingerMapper singerMapper;

    @Resource
    private CollectMapper collectMapper;

    @Resource
    private SongListMapper songListMapper;

    @Autowired
    private CollectService collectService;

    @Autowired
    private SongListService songListService;

    @Autowired
    private SongService songService;

    /**
     * 添加歌曲最近播放记录
     */
    @Override
    public Result addRecentSong(RecentSong recentSong) {
        LambdaQueryWrapper<RecentSong> lqw = new LambdaQueryWrapper<>();
        lqw.eq(RecentSong::getUserId, recentSong.getUserId()).eq(RecentSong::getSongId, recentSong.getSongId());
        RecentSong song = null;
        try {
            song = recentSongMapper.selectOne(lqw);
        } catch (Exception e) {
            return Result.error("查找数据时发生错误: " + e.getMessage());
        }
        if (Objects.nonNull(song)) {
            song.setCount(song.getCount() + 1);
            // 获取当前的LocalDateTime
            LocalDateTime now = LocalDateTime.now();
            // 将LocalDateTime转换为Date
            Date currentDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
            song.setUpdateTime(currentDate);
            int i = recentSongMapper.updateById(song);
            if (i > 0) {
                return Result.ok("添加成功");
            }
            else {
                return Result.error("添加失败");
            }
        }
        else {
//            recentSong.setId(1);
            recentSong.setCount(1);
            // 获取当前的LocalDateTime
            LocalDateTime now = LocalDateTime.now();
            // 将LocalDateTime转换为Date
            Date currentDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
            recentSong.setCreateTime(currentDate);
            recentSong.setUpdateTime(currentDate);
            try {
                int i = recentSongMapper.insert(recentSong);
                if (i > 0) {
                    return Result.ok("添加成功");
                }
                else {
                    return Result.error("添加失败");
                }
            } catch (Exception e) {
                return Result.error("插入数据时发生错误: " + e.getMessage());
            }
        }
    }

    /**
     * 获取当前用户最近播放列表
     */
    @Override
    public Result getRecentSongByUserId(Integer id) {
        List<RecentSongVo> recentSongByUserId = null;
        try {
            recentSongByUserId = recentSongMapper.getRecentSongByUserId(id);
        } catch (Exception e) {
            return Result.error("查找最近听歌数据时发生错误: " + e.getMessage());
        }
        return Result.ok("查询成功", recentSongByUserId);
    }

    /**
     * 推荐歌单
     */
    @Override
    public Result recommendSongList(Integer id) {
        HashSet<SongList> songListHashSet = new HashSet<>();
        List<SongList> songListByRecentSong = recentSongMapper.getSongListByRecentSong(id);
        List<Collect> tmpList = collectMapper.collectOfUserId((int) id);
        List<SongList> allCollectSongListByConsumerId = new ArrayList<>();
        tmpList.forEach(item -> {
            allCollectSongListByConsumerId.add(songListService.selectByPrimaryKey(item.getSongListId()));
        });

        if (!allCollectSongListByConsumerId.isEmpty()) {
            songListHashSet.addAll(allCollectSongListByConsumerId);
        }
        if (!songListByRecentSong.isEmpty()) {
            songListHashSet.addAll(songListByRecentSong);
        }
        if (songListHashSet.size() < 10) {
            List<SongList> allSongList = songListMapper.allSongList();
            if (!allSongList.isEmpty()) {
                for (SongList songList : allSongList) {
                    if (songListHashSet.size() == 10) {
                        break;
                    }
                    songListHashSet.add(songList);
                }
            }
        }
        return Result.ok("查询成功", songListHashSet);
    }

    /**
     * 推荐歌手
     */
    @Override
    public Result recommendSinger(Integer id) {
        HashSet<Singer> singerHashSet = new HashSet<>();
        List<Singer> singerByRecentSong = recentSongMapper.getSingerByRecentSong(id);
        if (!singerByRecentSong.isEmpty()) {
            singerHashSet.addAll(singerByRecentSong);
        }
        List<Collect> tmpList = collectMapper.collectOfUserId(id);
        List<Singer> singerByCollectSong = new ArrayList<>();
        tmpList.forEach(item -> {
            singerByCollectSong.add(singerMapper.selectByPrimaryKey(songService.selectByPrimaryKey(item.getSongId()).getSingerId()));
        });

        if (!singerByCollectSong.isEmpty()) {
            singerHashSet.addAll(singerByCollectSong);
        }
        if (singerHashSet.size() < 10) {
            List<Singer> singerList = singerMapper.allSinger();
            if (!singerList.isEmpty()) {
                for (Singer singer : singerList) {
                    if (singerHashSet.size() == 10) {
                        break;
                    }
                    singerHashSet.add(singer);
                }
            }
        }
        return Result.ok("查询成功", singerHashSet);
    }
}