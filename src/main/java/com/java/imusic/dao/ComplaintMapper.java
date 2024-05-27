package com.java.imusic.dao;

import com.java.imusic.domain.Comment;
import com.java.imusic.domain.Complaint;

import java.util.List;

public interface ComplaintMapper {
    /**
     *增加
     */
    public int insert(Complaint complaint);

    /**
     *修改
     */
    public int update(Complaint complaint);

    /**
     * 删除
     */
    public int delete(Integer id);
    public Complaint selectByPrimaryKey(Integer id);
    public List<Complaint> allComplaint();
    public List<Complaint> allComplaintSongByUser(Integer userId);
    public List<Complaint> allComplaintSongListByUser(Integer userId);
    public List<Complaint> allComplaintSongAgainstUser(Integer userId);
    public List<Complaint> allComplaintSongListAgainstUser(Integer userId);
}
