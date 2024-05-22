package com.java.imusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.imusic.domain.Follow;
import com.java.imusic.service.FollowService;
import com.java.imusic.service.impl.FollowServiceImpl;
import com.java.imusic.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addFollow(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        Integer singerId = Integer.parseInt(request.getParameter("singerId"));

        Follow follow = new Follow();
        follow.setUserId(userId);
        follow.setSingerId(singerId);

        boolean flag = followService.insert(follow)>0;
        if(!flag){
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "关注失败");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 1);
        jsonObject.put(Consts.MSG, "关注成功");
        return jsonObject;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteFollow(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Integer id = Integer.parseInt(request.getParameter("id"));           //id

        boolean flag = followService.delete(id)>0;
        if(!flag){
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "取消失败");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 1);
        jsonObject.put(Consts.MSG, "取消成功");
        return jsonObject;
    }

    @RequestMapping(value = "/selectByPrimaryKey", method = RequestMethod.GET)
    public Object selectByPrimaryKey(HttpServletRequest request) {
        String id = request.getParameter("id").trim();          //主键
        return followService.selectByPrimaryKey(Integer.parseInt(id));
    }

    @RequestMapping(value = "/selectByUserIdAndSingerId", method = RequestMethod.GET)
    public Object selectByUserIdAndSingerId(HttpServletRequest request) {
        Integer userId = Integer.parseInt(request.getParameter("userId").trim());
        Integer singerId = Integer.parseInt(request.getParameter("singerId").trim());
        return followService.selectByUserIdAndSingerId(userId,singerId);
    }

    @RequestMapping(value = "/getByUserId", method = RequestMethod.GET)
    public Object getByUserId(HttpServletRequest request) {
        Integer userId = Integer.parseInt(request.getParameter("userId").trim());
        return followService.getByUserId(userId);
    }

    @RequestMapping(value = "/getBySingerId", method = RequestMethod.GET)
    public Object getBySingerId(HttpServletRequest request) {
        Integer singerId = Integer.parseInt(request.getParameter("singerId").trim());
        return followService.getBySingerId(singerId);
    }

    @RequestMapping(value = "/getCountByUserId", method = RequestMethod.GET)
    public Object getCountByUserId(HttpServletRequest request) {
        Integer userId = Integer.parseInt(request.getParameter("userId").trim());
        return followService.getCountByUserId(userId);
    }

    @RequestMapping(value = "/getCountBySingerId", method = RequestMethod.GET)
    public Object getCountBySingerId(HttpServletRequest request) {
        Integer singerId = Integer.parseInt(request.getParameter("singerId").trim());
        return followService.getCountBySingerId(singerId);
    }
}
