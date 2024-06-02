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
    public List<Message> allMessage(Integer userId){
        return messageMapper.allMessage(userId);
    }
    @Override
    public List<Message> allMessageUnread(Integer userId){
        return messageMapper.allMessageUnread(userId);
    }
    @Override
    public List<Message> allMessageRead(Integer userId){
        return messageMapper.allMessageRead(userId);
    }
    @Override
    public Integer messageUnreadNum(Integer userId) {
        return messageMapper.messageUnreadNum(userId);
    }
    @Override
    public boolean read(Integer id){
        return messageMapper.read(id) > 0;
    }
}
