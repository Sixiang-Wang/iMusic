package com.java.extraPart.dao;

import com.java.extraPart.domain.Complaint;

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
    public int deleteBySongId(Integer songId);
    public int deleteBySongListId(Integer songListId);
    public Complaint selectByPrimaryKey(Integer id);
    public List<Complaint> allComplaint();

}
