package com.java.imusic.service;

import com.java.imusic.domain.Message;

import java.util.List;

public interface MessageService {
    public boolean insert(Message message);

    public boolean update(Message message);
    public boolean delete(Integer id);
    public Message selectByPrimaryKey(Integer id);
    public List<Message> allMessage(Integer userId);
    public List<Message> allMessageUnread(Integer userId);
    public List<Message> allMessageRead(Integer userId);
    public Integer messageUnreadNum(Integer userId);
    public boolean read(Integer id);
}
