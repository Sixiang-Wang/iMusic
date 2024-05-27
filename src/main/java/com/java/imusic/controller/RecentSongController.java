package com.java.imusic.controller;

import com.java.imusic.common.Result;
import com.java.imusic.domain.RecentSong;
import com.java.imusic.service.RecentSongService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/recentSong")
@RestController
public class RecentSongController {

    @Resource
    private RecentSongService recentSongService;

    /**
     * 添加歌曲最近播放记录
     */
    @PostMapping("/add")
    public Result addRecentSong(@RequestBody RecentSong recentSong) {
        return recentSongService.addRecentSong(recentSong);
    }

    /**
     * 获取当前用户最近播放列表
     */
    @GetMapping("/recentSongOfUserId/{id}")
    public Result getRecentSongByUserId(@PathVariable
                                            Integer id) {
        return recentSongService.getRecentSongByUserId(id);
    }

    /**
     * 推荐歌单
     */
    @GetMapping("/recommendSongList/{id}")
    public Result recommendSongList(@PathVariable
                                        Integer id) {
        return recentSongService.recommendSongList(id);
    }

    /**
     * 推荐歌手
     */
    @GetMapping("/recommendSinger/{id}")
    public Result recommendSinger(@PathVariable
                                      Integer id) {
        return recentSongService.recommendSinger(id);
    }
}
