package com.java.imusic.service.impl;

import com.java.imusic.dao.CommentMapper;
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
    public Complaint selectByPrimaryKey(Integer id) {
        return complaintMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Complaint> allComplaint() {
        return complaintMapper.allComplaint();
    }

    @Override
    public List<Complaint> allComplaintSongByUser(Integer userId) {
        return complaintMapper.allComplaintSongByUser(userId);
    }

    @Override
    public List<Complaint> allComplaintSongListByUser(Integer userId) {
        return complaintMapper.allComplaintSongListByUser(userId);
    }

    @Override
    public List<Complaint> allComplaintSongAgainstUser(Integer userId) {
        return complaintMapper.allComplaintSongAgainstUser(userId);
    }

    @Override
    public List<Complaint> allComplaintSongListAgainstUser(Integer userId) {
        return complaintMapper.allComplaintSongListAgainstUser(userId);
    }
}
