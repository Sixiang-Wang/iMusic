package com.java.messagepart.dao;

import com.java.messagepart.domain.Message;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMapper {
    public int insert(Message message);

    public int update(Message message);
    public int delete(Integer id);
    public Message selectByPrimaryKey(Integer id);
    public List<Message> allMessage(Integer to);
    public List<Message> allMessageUnread(Integer to);
    public List<Message> allMessageRead(Integer to);
    public Integer messageUnreadNum(Integer to);
    public List<Message> allUpMessage(Integer to);
    public List<Message> allUpMessageUnread(Integer to);
    public List<Message> allUpMessageRead(Integer to);
    public Integer upMessageUnreadNum(Integer to);
    public int read(Integer id);
}
