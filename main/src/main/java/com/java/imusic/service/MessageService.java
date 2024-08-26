package com.java.imusic.service;

import com.java.imusic.domain.Message;

import java.util.List;

public interface MessageService {
    public boolean insert(Message message);

    public boolean update(Message message);
    public boolean delete(Integer id);
    public Message selectByPrimaryKey(Integer id);
    public List<Message> allMessage(Integer to);
    public List<Message> allMessageUnread(Integer to);
    public List<Message> allMessageRead(Integer to);
    public Integer messageUnreadNum(Integer to);
    public boolean read(Integer id);
}
