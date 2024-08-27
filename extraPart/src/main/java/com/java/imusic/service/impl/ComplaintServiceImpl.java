package com.java.imusic.service.impl;

import com.java.imusic.dao.ComplaintMapper;
import com.java.imusic.domain.Complaint;
import com.java.imusic.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ComplaintServiceImpl implements ComplaintService {
    @Autowired
    private ComplaintMapper complaintMapper;
    @Override
    public boolean insert(Complaint complaint) {
        return complaintMapper.insert(complaint)>0;
    }

    @Override
    public boolean update(Complaint complaint) {
        return complaintMapper.update(complaint)>0;
    }

    @Override
    public boolean delete(Integer id) {
        return complaintMapper.delete(id)>0;
    }

    @Override
    public boolean deleteBySongId(Integer songId) {
        return complaintMapper.deleteBySongId(songId)>0;
    }

    @Override
    public Complaint selectByPrimaryKey(Integer id) {
        return complaintMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Complaint> allComplaint() {
        return complaintMapper.allComplaint();
    }
}
