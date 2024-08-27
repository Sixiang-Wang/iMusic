package com.java.songPart.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.songPart.domain.ListSong;
import com.java.songPart.domain.Song;
import com.java.songPart.service.ListSongService;
import com.java.songPart.service.SongService;
import com.java.songPart.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        String id = request.getParameter("id");
        String songId = request.getParameter("songId").trim();  //歌曲id
        String songListId = request.getParameter("songListId").trim(); //歌单id
        ListSong listSong = new ListSong();
        listSong.setSongId(Integer.parseInt(songId));
        listSong.setSongListId(Integer.parseInt(songListId));

        if (id!=null&&!id.isEmpty()){
            listSong.setId(Integer.parseInt(id));
        }

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




















