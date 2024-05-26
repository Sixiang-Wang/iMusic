package com.java.imusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.imusic.domain.CommentUp;
import com.java.imusic.service.CommentUpService;
import com.java.imusic.service.FollowService;
import com.java.imusic.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/commentUp")
public class CommentUpController {
    @Autowired
    private CommentUpService commentUpService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addCommentUp(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Integer userId = Integer.parseInt(request.getParameter("userId"));           //用户id
        Integer commentId = Integer.parseInt(request.getParameter("commentId"));

        CommentUp commentUp = new CommentUp();
        commentUp.setUserId(userId);
        commentUp.setCommentId(commentId);
        boolean flag = commentUpService.insert(commentUp);
        if(flag){   //保存成功
            jsonObject.put(Consts.CODE,1);
            jsonObject.put(Consts.MSG,"点赞成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE,0);
        jsonObject.put(Consts.MSG,"点赞失败");
        return jsonObject;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Object deleteCommentUp(HttpServletRequest request){
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        Integer commentId = Integer.parseInt(request.getParameter("commentId"));
        return commentUpService.delete(userId,commentId);
    }

    @RequestMapping(value = "/exist",method = RequestMethod.GET)
    public Object existCommentUp(HttpServletRequest request){
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        Integer commentId = Integer.parseInt(request.getParameter("commentId"));
        return commentUpService.exist(userId,commentId);
    }
}
