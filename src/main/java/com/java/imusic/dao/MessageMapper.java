package com.java.imusic.dao;

import com.java.imusic.domain.Message;
import com.java.imusic.domain.Rank;
import com.java.imusic.domain.SongList;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMapper {
    public int insert(Message message);

    public int update(Message message);
    public int delete(Integer id);
    public Message selectByPrimaryKey(Integer id);
    public List<Message> allMessage(Integer userId);
    public List<Message> allMessageUnread(Integer userId);
    public List<Message> allMessageRead(Integer userId);
    public Integer messageUnreadNum(Integer userId);
    public int read(Integer id);
}
