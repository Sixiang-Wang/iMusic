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
    public boolean deleteBySongId(Integer songId);
    public Complaint selectByPrimaryKey(Integer id);
    public List<Complaint> allComplaint();
}
