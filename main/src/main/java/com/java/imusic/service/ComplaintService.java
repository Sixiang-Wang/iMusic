package com.java.imusic.service;

import com.java.imusic.domain.Complaint;

import java.util.List;

public interface ComplaintService {
    /**
     *增加
     */
    public boolean insert(Complaint complaint);

    /**
     *修改
     */
    public boolean update(Complaint complaint);

    /**
     * 删除
     */
    public boolean delete(Integer id);
    public Complaint selectByPrimaryKey(Integer id);
    public List<Complaint> allComplaint();
    public List<Complaint> allComplaintSongByUser(Integer userId);
    public List<Complaint> allComplaintSongListByUser(Integer userId);
    public List<Complaint> allComplaintSongAgainstUser(Integer userId);
    public List<Complaint> allComplaintSongListAgainstUser(Integer userId);
}
