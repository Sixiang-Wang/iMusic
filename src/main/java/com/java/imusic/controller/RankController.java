package com.java.imusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.imusic.domain.Rank;
import com.java.imusic.service.RankService;
import com.java.imusic.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RankController {

    @Autowired
    private RankService rankService;

    /**
     * 新增评价
     */
    @RequestMapping(value = "/rank/add",method = RequestMethod.POST)
    public Object add(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        String songListId = request.getParameter("songListId");
        String userId = request.getParameter("userId");
        String score = request.getParameter("score");

        Rank rank = new Rank();
        rank.setSongListId(Integer.parseInt(songListId));
        rank.setUserId(Integer.parseInt(userId));


        Rank rankNew = rankService.getRank(rank);
        if(rankNew != null){
            rankNew.setScore(Integer.parseInt(score));
            boolean flag = rankService.update(rankNew);
            if(flag){
                jsonObject.put(Consts.CODE,1);
                jsonObject.put(Consts.MSG,"评价更新成功");
                return jsonObject;
            }
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"评价更新失败");
            return jsonObject;
        }

        rank.setScore(Integer.parseInt(score));
        boolean flag = rankService.insert(rank);
        if(flag){
            jsonObject.put(Consts.CODE,1);
            jsonObject.put(Consts.MSG,"评价成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE,0);
        jsonObject.put(Consts.MSG,"评价失败");
        return jsonObject;
    }
    @RequestMapping(value = "/rank/rankOfSongListIdAndUserId",method = RequestMethod.GET)
    public Object rankOfSongListIdAndUserId(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        String songListId = request.getParameter("songListId");
        String userId = request.getParameter("UserId");
        Rank rank = new Rank();
        rank.setSongListId(Integer.parseInt(songListId));
        rank.setUserId(Integer.parseInt(userId));
        Rank rankNew =  rankService.getRank(rank);
        if(rankNew==null){
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"无评分");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE,1);
        jsonObject.put(Consts.MSG,"查询成功");
        jsonObject.put("rank",rankNew.getScore());
        return jsonObject;
    }

    /**
     * 计算平均分
     */
    @RequestMapping(value = "/rank/rankOfSongListId",method = RequestMethod.GET)
    public Object rankOfSongListId(HttpServletRequest request){
        String songListId = request.getParameter("songListId");
        return rankService.rankOfSongListId(Integer.parseInt(songListId));
    }
}






















