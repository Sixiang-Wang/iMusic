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
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageMapper messageMapper;

    /**
     * 添加信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addMessage(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Integer to = Integer.parseInt(request.getParameter("to"));
        String text = request.getParameter("text");
        String typeS = request.getParameter("type");
        int type = 0; //普通消息
        if (typeS!=null&&!typeS.isEmpty()){
            type = Integer.parseInt(typeS);
        }

        //可有可无
        String fromTmp = request.getParameter("from");
        int from = -1;
        if(fromTmp!=null && !fromTmp.isEmpty()){
            from = Integer.parseInt(fromTmp);
        }

        Message message = new Message();
        message.setTo(to);
        message.setFrom(from);
        message.setText(text);
        message.setIsRead(0);
        message.setType(type);

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
        String typeS = request.getParameter("type");
        int type = 0; //普通消息
        if (typeS!=null&&!typeS.isEmpty()){
            type = Integer.parseInt(typeS);
        }

        Message message = new Message();
        message.setId(id);
        message.setText(text);
        message.setType(type);
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

    @RequestMapping(value = "/deleteAllReadMessage",method = RequestMethod.GET)
    public Object deleteAllReadMessage(HttpServletRequest request){
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        List<Message> messageList = messageService.allMessageRead(userId);
        AtomicReference<Boolean> flag = new AtomicReference<>(true);
        messageList.forEach(message -> {
            if(!messageService.delete(message.getId()))
                flag.set(false);
        });
        return flag;
    }


    /**
     * 所有系统信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/allMessage",method = RequestMethod.GET)
    public Object allMessage(HttpServletRequest request){
        Integer to = Integer.parseInt(request.getParameter("to"));
        return messageService.allMessage(to);
    }

    /**
     * 所有未读系统信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/allMessageUnread",method = RequestMethod.GET)
    public Object allMessageUnread(HttpServletRequest request){
        Integer to = Integer.parseInt(request.getParameter("to"));
        return messageService.allMessageUnread(to);
    }

    /**
     * 所有已读系统信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/allMessageRead",method = RequestMethod.GET)
    public Object allMessageRead(HttpServletRequest request){
        Integer to = Integer.parseInt(request.getParameter("to"));
        return messageService.allMessageRead(to);
    }

    /**
     * 未读系统消息数量
     * @param request
     * @return
     */
    @RequestMapping(value = "/messageUnreadNum",method = RequestMethod.GET)
    public Object messageUnreadNum(HttpServletRequest request){
        Integer to = Integer.parseInt(request.getParameter("to"));
        return messageService.messageUnreadNum(to);
    }

    /**
     * 所有点赞信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/allUpMessage",method = RequestMethod.GET)
    public Object allUpMessage(HttpServletRequest request){
        Integer to = Integer.parseInt(request.getParameter("to"));
        return messageMapper.allUpMessage(to);
    }

    /**
     * 所有未读点赞信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/allUpMessageUnread",method = RequestMethod.GET)
    public Object allUpMessageUnread(HttpServletRequest request){
        Integer to = Integer.parseInt(request.getParameter("to"));
        return messageMapper.allUpMessageUnread(to);
    }

    /**
     * 所有已读点赞信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/allUpMessageRead",method = RequestMethod.GET)
    public Object allUpMessageRead(HttpServletRequest request){
        Integer to = Integer.parseInt(request.getParameter("to"));
        return messageMapper.allUpMessageRead(to);
    }

    /**
     * 未读点赞消息数量
     * @param request
     * @return
     */
    @RequestMapping(value = "/upMessageUnreadNum",method = RequestMethod.GET)
    public Object upMessageUnreadNum(HttpServletRequest request){
        Integer to = Integer.parseInt(request.getParameter("to"));
        return messageMapper.upMessageUnreadNum(to);
    }
}
