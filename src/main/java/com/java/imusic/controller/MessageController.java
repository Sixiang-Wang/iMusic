package com.java.imusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.imusic.dao.MessageMapper;
import com.java.imusic.domain.Message;
import com.java.imusic.service.MessageService;
import com.java.imusic.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    /**
     * 添加信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addMessage(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Integer userId = Integer.parseInt(request.getParameter("userId"));           //用户id
        String text = request.getParameter("text");

        Message message = new Message();
        message.setUserId(userId);
        message.setText(text);
        message.setIsRead(0);

        boolean flag = messageService.insert(message);

        if(flag){   //保存成功
            jsonObject.put(Consts.CODE,1);
            return jsonObject;
        }
        jsonObject.put(Consts.CODE,0);
        return jsonObject;
    }

    /**
     * 更新信息（备用）
     * @param request
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object updateMessage(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Integer id = Integer.parseInt(request.getParameter("id"));
        String text = request.getParameter("text");
        Message message = new Message();
        message.setId(id);
        message.setText(text);
        boolean flag = messageService.update(message);
        if(flag){   //保存成功
            jsonObject.put(Consts.CODE,1);
            return jsonObject;
        }
        jsonObject.put(Consts.CODE,0);
        return jsonObject;
    }

    /**
     * !!!!!!!!!!!!!!!
     * 让信息变成已读
     * @param request
     * @return
     */
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public Object readMessage(HttpServletRequest request) {
        Integer id = Integer.parseInt(request.getParameter("id"));
        return messageService.read(id);
    }



    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Object deleteMessage(HttpServletRequest request){
        Integer id = Integer.parseInt(request.getParameter("id"));
        return messageService.delete(id);
    }


    /**
     * 所有信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/allMessage",method = RequestMethod.GET)
    public Object allMessage(HttpServletRequest request){
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        return messageService.allMessage(userId);
    }

    /**
     * 所有未读信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/allMessageUnread",method = RequestMethod.GET)
    public Object allMessageUnread(HttpServletRequest request){
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        return messageService.allMessageUnread(userId);
    }

    /**
     * 所有已读信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/allMessageRead",method = RequestMethod.GET)
    public Object allMessageRead(HttpServletRequest request){
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        return messageService.allMessageRead(userId);
    }

    /**
     * 未读消息数量
     * @param request
     * @return
     */
    @RequestMapping(value = "/messageUnreadNum",method = RequestMethod.GET)
    public Object messageUnreadNum(HttpServletRequest request){
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        return messageService.messageUnreadNum(userId);
    }
}
