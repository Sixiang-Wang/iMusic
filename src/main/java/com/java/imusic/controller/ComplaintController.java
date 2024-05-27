package com.java.imusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.imusic.domain.Complaint;
import com.java.imusic.service.ComplaintService;
import com.java.imusic.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 评论控制类
 */
@RestController
@RequestMapping("/complaint")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    /**
     * 添加评论
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Object addComplaint(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        String userId = request.getParameter("userId");           //用户id
        String type = request.getParameter("type");               //评论类型（0歌曲1歌单）
        String songId = request.getParameter("songId");           //歌曲id
        String songListId = request.getParameter("songListId");   //歌单id
        String content = request.getParameter("content").trim();

        Complaint complaint = new Complaint();
        complaint.setUserId(Integer.parseInt(userId));
        complaint.setType(new Byte(type));
        if(new Byte(type) ==0){
            complaint.setSongId(Integer.parseInt(songId));
        }else{
            complaint.setSongListId(Integer.parseInt(songListId));
        }
        complaint.setContent(content);
        boolean flag = complaintService.insert(complaint);
        if(flag){   //保存成功
            jsonObject.put(Consts.CODE,1);
            jsonObject.put(Consts.MSG,"成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE,0);
        jsonObject.put(Consts.MSG,"失败");
        return jsonObject;
    }

    /**
     * 修改评论
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Object updateComplaint(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim();                   //主键
        String userId = request.getParameter("userId").trim();           //用户id
        String type = request.getParameter("type").trim();               //评论类型（0歌曲1歌单）
        String songId = request.getParameter("songId").trim();           //歌曲id
        String songListId = request.getParameter("songListId").trim();   //歌单id
        String content = request.getParameter("content").trim();         //评论内容

        //保存到评论的对象中
        Complaint complaint = new Complaint();
        complaint.setId(Integer.parseInt(id));
        complaint.setUserId(Integer.parseInt(userId));
        complaint.setType(new Byte(type));
        if(songId!=null&&songId.equals("")){
            songId = null;
        }else {
            complaint.setSongId(Integer.parseInt(songId));
        }
        if(songListId!=null&&songListId.equals("")){
            songListId = null;
        }else {
            complaint.setSongListId(Integer.parseInt(songListId));
        }
        complaint.setContent(content);

        boolean flag = complaintService.update(complaint);
        if(flag){   //保存成功
            jsonObject.put(Consts.CODE,1);
            jsonObject.put(Consts.MSG,"修改成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE,0);
        jsonObject.put(Consts.MSG,"修改失败");
        return jsonObject;
    }

    /**
     * 删除评论
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Object deleteComplaint(HttpServletRequest request){
        String id = request.getParameter("id").trim();          //主键
        boolean flag = complaintService.delete(Integer.parseInt(id));
        return flag;
    }

    /**
     * 根据主键查询整个对象
     */
    @RequestMapping(value = "/selectByPrimaryKey",method = RequestMethod.GET)
    public Object selectByPrimaryKey(HttpServletRequest request){
        String id = request.getParameter("id").trim();          //主键
        return complaintService.selectByPrimaryKey(Integer.parseInt(id));
    }

    /**
     * 查询所有评论
     */
    @RequestMapping(value = "/allComplaint",method = RequestMethod.GET)
    public Object allComplaint(HttpServletRequest request){
        return complaintService.allComplaint();
    }

    @RequestMapping(value = "/allComplaintByUser",method = RequestMethod.GET)
    public Object allComplaintByUser(HttpServletRequest request){
        Integer userId = Integer.parseInt(request.getParameter("userId").trim());
        System.out.println(userId);
        List<Complaint> complaintList = complaintService.allComplaintSongByUser(userId);
        List<Complaint> complaintList2 = complaintService.allComplaintSongListByUser(userId);
        complaintList.addAll(complaintList2);
        return complaintList;
    }

    @RequestMapping(value = "/allComplaintAgainstUser",method = RequestMethod.GET)
    public Object allComplaintAgainstUser(HttpServletRequest request){
        Integer userId = Integer.parseInt(request.getParameter("userId").trim());
        List<Complaint> complaintList = complaintService.allComplaintSongAgainstUser(userId);
        List<Complaint> complaintList2 = complaintService.allComplaintSongListAgainstUser(userId);
        complaintList.addAll(complaintList2);
        return complaintList;
    }

}






















