package com.java.songPart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.songPart.common.Result;
import com.java.songPart.domain.*;
import com.java.songPart.dao.*;
import com.java.songPart.service.RecentSongService;
import com.java.songPart.service.SongListService;
import com.java.songPart.service.SongService;
import com.java.songPart.utils.Port;
import com.java.songPart.vo.RecentSongVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
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
    private SongListMapper songListMapper;

    @Autowired
    private SongListService songListService;

    @Autowired
    private SongService songService;

    @Autowired
    private RestTemplate restTemplate;


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
        try {
            List<RecentSongVo> recentSongByUserId = recentSongMapper.getRecentSongByUserId(id);
            recentSongByUserId = addSingerName(recentSongByUserId);
            return Result.ok("查询成功", recentSongByUserId);
        } catch (Exception e) {
            return Result.error("查找最近听歌数据时发生错误: " + e.getMessage());
        }
    }


    /**
     * 获取指定用户最近播放列表，按播放量降序排列
     */
    @Override
    public Result getRecentSongByUserIdOrderByCountDesc(Integer id) {
        try {
            List<RecentSongVo> recentSongByUserId = recentSongMapper.getRecentSongByUserIdOrderByCountDesc(id);
            recentSongByUserId = addSingerName(recentSongByUserId);
            return Result.ok("查询成功", recentSongByUserId);
        } catch (Exception e) {
            return Result.error("查找听歌数据时发生错误: " + e.getMessage());
        }
    }


    /**
     * 推荐歌单
     */
    @Override
    public Result recommendSongList(Integer id) {
        HashSet<SongList> songListHashSet = new HashSet<>();
        List<SongList> songListByRecentSong = recentSongMapper.getSongListByRecentSong(id);

//        List<Collect> tmpList = collectMapper.collectOfUserId(id);
        List<Collect> tmpList = restTemplate.getForObject(
                Port.url_base + Port.port_base + "/collect/collectOfUserId?userId=" + id,
                List.class
        );
        List<SongList> allCollectSongListByConsumerId = new ArrayList<>();
        if (tmpList != null)
        {
            tmpList.forEach(item -> {
                if (item.getSongListId() != null) {
                    allCollectSongListByConsumerId.add(songListService.selectByPrimaryKey(item.getSongListId()));
                }
            });
        }

        if (!allCollectSongListByConsumerId.isEmpty()) {
            songListHashSet.addAll(allCollectSongListByConsumerId);
        }
        if (!songListByRecentSong.isEmpty()) {
            for (SongList songList : songListByRecentSong) {
                int flag = 0;
                for (SongList songList1 : songListHashSet) {
                    if (songList1.getId().equals(songList.getId())) {
                        flag = 1;
                        break;
                    }
                }
                if (0 == flag) {
                    songListHashSet.add(songList);
                }
            }
        }
        if (songListHashSet.size() < 10) {
            List<SongList> allSongList = songListMapper.allSongList();
            if (!allSongList.isEmpty()) {
                for (SongList songList : allSongList) {
                    if (songListHashSet.size() == 10) {
                        break;
                    }
                    int flag = 0;
                    for (SongList songList1 : songListHashSet) {
                        if (songList1.getId().equals(songList.getId())) {
                            flag = 1;
                            break;
                        }
                    }
                    if (0 == flag) {
                        songListHashSet.add(songList);
                    }
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
        HashSet<User> singerHashSet = new HashSet<>();
        List<Integer> singerIdByRecentSong = recentSongMapper.getSingerByRecentSong(id);
        List<User> singerByRecentSong = getUserListById(singerIdByRecentSong);
        if (!singerByRecentSong.isEmpty()) {
            singerHashSet.addAll(singerByRecentSong);
        }
        List<Collect> tmpList = restTemplate.getForObject(
                Port.url_base + Port.port_base + "/collect/collectOfUserId?userId=" + id,
                List.class
        );
//        List<Collect> tmpList = collectMapper.collectOfUserId(id);
        List<User> singerByCollectSong = new ArrayList<>();
        if (tmpList != null)
        {
            tmpList.forEach(item -> {
                if (item.getSongId() != null) {
                    User user = restTemplate.getForObject(
                            Port.url_base+Port.port_base+"/user/selectByPrimaryKey?id="+songService.selectByPrimaryKey(item.getSongId()).getUserId(),
                            User.class
                    );
//                    singerByCollectSong.add(userMapperser.selectByPrimaryKey(songService.selectByPrimaryKey(item.getSongId()).getUserId()));
                    singerByCollectSong.add(user);
                }
            });
        }

        if (!singerByCollectSong.isEmpty()) {
            for (User singer : singerByCollectSong) {
                int flag = 0;
                for (User singer1 : singerHashSet) {
                    if (singer1.getId().equals(singer.getId())) {
                        flag = 1;
                        break;
                    }
                }
                if (0 == flag) {
                    singerHashSet.add(singer);
                }
            }
        }
        if (singerHashSet.size() < 10) {
//            List<User> singerList = userMapperser.allUser();
            List<User> singerList = restTemplate.getForObject(
                    Port.url_base+Port.port_base+"/user/allUser",
                    List.class
            );
            if (singerList != null && !singerList.isEmpty())
            {
                for (User singer : singerList)
                {
                    if (singerHashSet.size() == 10)
                    {
                        break;
                    }

                    int flag = 0;
                    for (User singer1 : singerHashSet)
                    {
                        if (singer1.getId().equals(singer.getId()))
                        {
                            flag = 1;
                            break;
                        }
                    }
                    if (0 == flag)
                    {
                        singerHashSet.add(singer);
                    }
                }
            }
        }
        return Result.ok("查询成功", singerHashSet);
    }

    // 添加歌手名
    private List<RecentSongVo> addSingerName(List<RecentSongVo> recentSong) {
        for (RecentSongVo recentSongVo : recentSong) {
            User user = restTemplate.getForObject(
                    Port.url_base+Port.port_base+"/user/selectByPrimaryKey?id="+recentSongVo.getSingerId(),
                    User.class
            );
//            User user = UserService.getUserWithID(recentSongVo.getSingerId());
            if (user != null)
            {
                recentSongVo.setSingerName(user.getName());
            }
        }
        return recentSong;
    }

    // 根据Id列表获取User列表
    private List<User> getUserListById(List<Integer> idList) {
        List<User> userList = new ArrayList<>();
        for (Integer id : idList) {
            User user = restTemplate.getForObject(
                    Port.url_base+Port.port_base+"/user/selectByPrimaryKey?id="+id,
                    User.class
            );
            userList.add(user);
        }
        return userList;
    }
}
