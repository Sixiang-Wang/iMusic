package com.java.imusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.imusic.domain.Collect;
import com.java.imusic.domain.Song;
import com.java.imusic.service.CollectService;
import com.java.imusic.service.SongService;
import com.java.imusic.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 收藏控制类
 */
@RestController
@RequestMapping("/collect")
public class CollectController {

    //推荐数量
    public static int cfCount = 5;
    @Autowired
    private CollectService collectService;
    @Autowired
    private SongService songService;

    /**
     * 添加收藏
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addCollect(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String userId = request.getParameter("userId");           //用户id
        String type = request.getParameter("type");               //收藏类型（0歌曲1歌单）
        String songId = request.getParameter("songId");           //歌曲id
        String songListId = request.getParameter("songListId");
        if ((songId == null || songId.isEmpty()) && (songListId == null || songListId.isEmpty())) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "收藏失败");
            return jsonObject;
        }
        Byte typeByte = new Byte(type);

        if (typeByte==0 && collectService.existSongId(Integer.parseInt(userId), Integer.parseInt(songId))) {
            jsonObject.put(Consts.CODE, 2);
            jsonObject.put(Consts.MSG, "已收藏");
            return jsonObject;
        } else if(typeByte==1 && collectService.existSongListId(Integer.parseInt(userId), Integer.parseInt(songListId))){
            jsonObject.put(Consts.CODE, 2);
            jsonObject.put(Consts.MSG, "已收藏");
            return jsonObject;
        }

        //保存到收藏的对象中
        Collect Collect = new Collect();
        Collect.setUserId(Integer.parseInt(userId));
        Collect.setType(typeByte);
        if(typeByte == 0)
            Collect.setSongId(Integer.parseInt(songId));
        else if(typeByte == 1)
            Collect.setSongListId(Integer.parseInt(songListId));

        boolean flag = collectService.insert(Collect);
        if (flag) {   //保存成功
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "收藏成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "收藏失败");
        return jsonObject;
    }

    /**
     * 删除收藏
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteCollect(HttpServletRequest request) {
        String userId = request.getParameter("userId");           //用户id
        String songId = request.getParameter("songId");           //歌曲id
        boolean flag = collectService.deleteByUserIdSongId(Integer.parseInt(userId), Integer.parseInt(songId));
        return flag;
    }

    /**
     * 查询所有收藏
     */
    @RequestMapping(value = "/allCollect", method = RequestMethod.GET)
    public Object allCollect(HttpServletRequest request) {
        return collectService.allCollect();
    }

    /**
     * 查询某个用户的收藏列表
     */
    @RequestMapping(value = "/collectOfUserId", method = RequestMethod.GET)
    public Object collectOfUserId(HttpServletRequest request) {
        String userId = request.getParameter("userId");          //用户id
        return collectService.collectOfUserId(Integer.parseInt(userId));
    }

    /**
     * 查询某个用户的推荐歌曲列表
     */
    @RequestMapping(value = "/collectSongOfUserId", method = RequestMethod.GET)
    public Object collectSongOfUserId(HttpServletRequest request) {
        String uId = request.getParameter("userId");

        int userId = -1;
        if (!uId.isEmpty()) {
            userId = Integer.parseInt(uId);
        }

        if (userId > 0) {
            // 查到所有用户的收藏列表
            List<Collect> list = collectService.allCollect();
            List<Song> collectSong = new ArrayList<>();
            for (Collect collect : list) {
                if(collect.getType()==0){
                    collectSong.add(songService.selectByPrimaryKey(collect.getSongId()));
                }
            }

            return collectSong;
        }
        return null;
    }

}






















