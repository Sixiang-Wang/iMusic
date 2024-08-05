package com.java.imusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.imusic.domain.ListSong;
import com.java.imusic.domain.Song;
import com.java.imusic.service.ListSongService;
import com.java.imusic.service.SongService;
import com.java.imusic.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 歌单的歌曲管理controller
 */
@RestController
@RequestMapping("/listSong")
public class ListSongController {

    @Autowired
    private ListSongService listSongService;
    @Autowired
    private SongService songService;

    /**
     * 给歌单添加歌曲
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addListSong(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        //获取前端传来的参数
        String songId = request.getParameter("songId").trim();  //歌曲id
        String songListId = request.getParameter("songListId").trim(); //歌单id
        ListSong listSong = new ListSong();
        listSong.setSongId(Integer.parseInt(songId));
        listSong.setSongListId(Integer.parseInt(songListId));
        boolean flag = listSongService.insert(listSong);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "保存成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "保存失败");
        return jsonObject;

    }

    /**
     * 根据歌单id查询歌曲
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Object detail(HttpServletRequest request) {
        String songListId = request.getParameter("songListId");
        return listSongService.listSongOfSongListId(Integer.parseInt(songListId));
    }

    @RequestMapping(value = "/allSong", method = RequestMethod.GET)
    public Object allSong(HttpServletRequest request) {
        String songListId = request.getParameter("songListId");
        List<ListSong> listSongs =  listSongService.listSongOfSongListId(Integer.parseInt(songListId));
        List<Song> songs = new ArrayList<>();
        listSongs.forEach(listSong -> {
            songs.add(songService.selectByPrimaryKey(listSong.getSongId()));
        });
        return songs;
    }


    /**
     * 删除歌单里的歌曲
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object delete(HttpServletRequest request) {
        String songId = request.getParameter("songId").trim();                 //歌曲id
        String songListId = request.getParameter("songListId").trim();        //歌单id
        boolean flag = listSongService.deleteBySongIdAndSongListId(Integer.parseInt(songId), Integer.parseInt(songListId));
        return flag;
    }

    /*
      我们不搞会员制餐厅。
     */

}




















