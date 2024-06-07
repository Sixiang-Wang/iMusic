package com.java.imusic.service.impl;

import com.java.imusic.dao.MessageMapper;
import com.java.imusic.domain.Message;
import com.java.imusic.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Override
    public boolean insert(Message message){
        return messageMapper.insert(message) > 0;
    }

    @Override
    public boolean update(Message message){
        return messageMapper.update(message) > 0;
    }
    @Override
    public boolean delete(Integer id){
        return messageMapper.delete(id) > 0;
    }
    @Override
    public Message selectByPrimaryKey(Integer id){
        return messageMapper.selectByPrimaryKey(id);
    }
    @Override
    public List<Message> allMessage(Integer to){
        return messageMapper.allMessage(to);
    }
    @Override
    public List<Message> allMessageUnread(Integer to){
        return messageMapper.allMessageUnread(to);
    }
    @Override
    public List<Message> allMessageRead(Integer to){
        return messageMapper.allMessageRead(to);
    }
    @Override
    public Integer messageUnreadNum(Integer to) {
        return messageMapper.messageUnreadNum(to);
    }
    @Override
    public boolean read(Integer id){
        return messageMapper.read(id) > 0;
    }
}
