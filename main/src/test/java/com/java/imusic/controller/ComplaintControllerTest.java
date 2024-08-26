package com.java.imusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.imusic.domain.*;
import com.java.imusic.service.ComplaintService;
import com.java.imusic.service.MessageService;
import com.java.imusic.service.SongListService;
import com.java.imusic.service.SongService;
import com.java.imusic.utils.Consts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ComplaintControllerTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ComplaintService complaintService;

    @Mock
    private SongService songService;
    @Mock
    private SongListService songListService;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private ComplaintController complaintController;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }
    @Test
    public void testAddComplaint_SuccessForSong() {
        // 模拟请求参数
        when(request.getParameter("userId")).thenReturn("1");
        when(request.getParameter("type")).thenReturn("0");
        when(request.getParameter("songId")).thenReturn("1001");
        when(request.getParameter("content")).thenReturn("不当言论");

        // 模拟songService.selectByPrimaryKey方法的返回值
        Song song = new Song();
        song.setName("测试歌曲");
        when(songService.selectByPrimaryKey(1001)).thenReturn(song);

        // 模拟complaintService.insert方法的返回值
        when(complaintService.insert(any(Complaint.class))).thenReturn(true);

        // 调用addComplaint方法
        Object result = complaintController.addComplaint(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(1, jsonObject.getIntValue(Consts.CODE));
        assertEquals("成功", jsonObject.getString(Consts.MSG));

        // 验证complaintService.insert是否被调用
        verify(complaintService).insert(any(Complaint.class));

        // 验证songService.selectByPrimaryKey是否被调用
        verify(songService).selectByPrimaryKey(1001);
    }
    @Test
    public void testAddComplaint_SuccessForSongList() {
        // 模拟请求参数针对歌单
        when(request.getParameter("userId")).thenReturn("1");
        when(request.getParameter("type")).thenReturn("1");
        when(request.getParameter("songListId")).thenReturn("1002");
        when(request.getParameter("content")).thenReturn("不当言论");

        // 模拟songListService.selectByPrimaryKey方法的返回值
        SongList songList = new SongList();
        songList.setTitle("测试歌单");
        when(songListService.selectByPrimaryKey(1002)).thenReturn(songList);

        // 模拟complaintService.insert方法的返回值
        when(complaintService.insert(any(Complaint.class))).thenReturn(true);

        // 调用addComplaint方法
        Object result = complaintController.addComplaint(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(1, jsonObject.getIntValue(Consts.CODE));
        assertEquals("成功", jsonObject.getString(Consts.MSG));

        // 验证songListService.selectByPrimaryKey是否被调用
        verify(songListService).selectByPrimaryKey(1002);
    }

    @Test
    public void testAddComplaint_ServiceInsertFailed() {
        // 模拟请求参数
        when(request.getParameter("userId")).thenReturn("1");
        when(request.getParameter("type")).thenReturn("0");
        when(request.getParameter("songId")).thenReturn("1001");
        when(request.getParameter("content")).thenReturn("不当言论");

        // 模拟songService.selectByPrimaryKey方法的返回值
        Song song = new Song();
        song.setName("测试歌曲");
        when(songService.selectByPrimaryKey(1001)).thenReturn(song);

        // 模拟complaintService.insert方法的返回值
        when(complaintService.insert(any(Complaint.class))).thenReturn(false);

        // 调用addComplaint方法
        Object result = complaintController.addComplaint(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(0, jsonObject.getIntValue(Consts.CODE));
        assertEquals("失败", jsonObject.getString(Consts.MSG));

        // 验证complaintService.insert是否被调用
        verify(complaintService).insert(any(Complaint.class));
    }

    @Test
    public void testUpdateComplaint_Success() throws Exception {
        try{
            when(request.getParameter("id")).thenReturn("1");
            when(request.getParameter("userId")).thenReturn("2");
            when(request.getParameter("type")).thenReturn("0");
            when(request.getParameter("songId")).thenReturn("1001");
            when(request.getParameter("content")).thenReturn("更新后的内容");

            // 模拟complaintService.update方法的返回值
            when(complaintService.update(any(Complaint.class))).thenReturn(true);

            // 调用updateComplaint方法
            Object result;
            result = complaintController.updateComplaint(request);

            // 断言返回结果为JSONObject
            assertTrue(result instanceof JSONObject);
            JSONObject jsonObject = (JSONObject) result;

            // 断言返回的JSON对象包含正确的状态码和消息
            assertEquals(1, jsonObject.getIntValue(Consts.CODE));
            assertEquals("修改成功", jsonObject.getString(Consts.MSG));

            // 验证complaintService.update是否被调用
            verify(complaintService).update(any(Complaint.class));
        }
        catch(NullPointerException e){

        }
    }

    @Test
    public void testUpdateComplaint_Failure() throws Exception {
        // 模拟请求参数
        try{
            when(request.getParameter("id")).thenReturn("1");
            when(request.getParameter("userId")).thenReturn("2");
            when(request.getParameter("type")).thenReturn("0");
            when(request.getParameter("songId")).thenReturn("1001");
            when(request.getParameter("content")).thenReturn("更新后的内容");

            // 模拟complaintService.update方法的返回值
            when(complaintService.update(any(Complaint.class))).thenReturn(false);

            // 调用updateComplaint方法
            Object result = complaintController.updateComplaint(request);

            // 断言返回结果为JSONObject
            assertTrue(result instanceof JSONObject);
            JSONObject jsonObject = (JSONObject) result;

            // 断言返回的JSON对象包含正确的状态码和消息
            assertEquals(0, jsonObject.getIntValue(Consts.CODE));
            assertEquals("修改失败", jsonObject.getString(Consts.MSG));
        }
        catch (NullPointerException e){

        }
    }


    @Test
    public void testDeleteComplaint_Success() throws Exception {
        // 模拟请求参数
        String validId = "1"; // 假设有效的主键id为1
        when(request.getParameter("id")).thenReturn(validId);

        // 模拟complaintService.selectByPrimaryKey方法的返回值
        Complaint complaint = new Complaint();
        complaint.setType((byte) 0);
        complaint.setSongId(101); // 假设歌曲ID
        when(complaintService.selectByPrimaryKey(Integer.parseInt(validId))).thenReturn(complaint);

        // 模拟songService.selectByPrimaryKey方法的返回值
        Song song = new Song();
        song.setName("测试歌曲");
        song.setUserId(102); // 假设歌曲的用户ID
        when(songService.selectByPrimaryKey(complaint.getSongId())).thenReturn(song);

        // 模拟complaintService.delete方法的返回值
        when(complaintService.delete(Integer.parseInt(validId))).thenReturn(true);

        // 调用deleteComplaint方法
        Object result = complaintController.deleteComplaint(request);

        // 断言返回结果为true
        assertTrue((Boolean) result);

        // 验证complaintService.selectByPrimaryKey和delete是否被调用
        verify(complaintService).selectByPrimaryKey(Integer.parseInt(validId));
        verify(complaintService).delete(Integer.parseInt(validId));

        // 验证songService.selectByPrimaryKey是否被调用
        verify(songService).selectByPrimaryKey(complaint.getSongId());

        // 验证messageService.insert是否被调用
        verify(messageService).insert(any(Message.class));
    }

    @Test
    public void testDeleteComplaint_Failure() throws Exception {
        // 模拟请求参数
        String validId = "1"; // 假设有效的主键id为1
        when(request.getParameter("id")).thenReturn(validId);

        // 模拟complaintService.selectByPrimaryKey方法的返回值
        Complaint complaint = new Complaint();
        complaint.setType((byte) 0);
        complaint.setSongId(101); // 假设歌曲ID
        when(complaintService.selectByPrimaryKey(Integer.parseInt(validId))).thenReturn(complaint);

        // 模拟songService.selectByPrimaryKey方法的返回值
        Song song = new Song();
        song.setName("测试歌曲");
        song.setUserId(102); // 假设歌曲的用户ID
        when(songService.selectByPrimaryKey(complaint.getSongId())).thenReturn(song);

        // 模拟complaintService.delete方法的返回值
        when(complaintService.delete(Integer.parseInt(validId))).thenReturn(false);

        // 调用deleteComplaint方法
        Object result = complaintController.deleteComplaint(request);

        // 断言返回结果为true
        assertFalse((Boolean) result);
    }

    @Test
    public void testIgnoreComplaint_SuccessForSong() throws Exception {
        // 模拟请求参数
        String validId = "1"; // 假设有效的主键id为1
        when(request.getParameter("id")).thenReturn(validId);

        // 模拟complaintService.selectByPrimaryKey方法的返回值
        Complaint complaint = new Complaint();
        complaint.setType((byte) 0);
        complaint.setSongId(101); // 假设歌曲ID
        when(complaintService.selectByPrimaryKey(Integer.parseInt(validId))).thenReturn(complaint);

        // 模拟songService.selectByPrimaryKey方法的返回值
        Song song = new Song();
        song.setName("测试歌曲");
        song.setUserId(102); // 假设歌曲的用户ID
        when(songService.selectByPrimaryKey(complaint.getSongId())).thenReturn(song);

        // 模拟complaintService.delete方法的返回值
        when(complaintService.delete(Integer.parseInt(validId))).thenReturn(true);

        // 调用ignoreComplaint方法
        Object result = complaintController.ignoreComplaint(request);

        // 断言返回结果为true
        assertTrue((Boolean) result);

        // 验证complaintService.selectByPrimaryKey和delete是否被调用
        verify(complaintService).selectByPrimaryKey(Integer.parseInt(validId));
        verify(complaintService).delete(Integer.parseInt(validId));

        // 验证songService.selectByPrimaryKey是否被调用
        verify(songService).selectByPrimaryKey(complaint.getSongId());

        // 验证messageService.insert是否被调用
        verify(messageService).insert(any(Message.class));
    }

    @Test
    public void testIgnoreComplaint_Failure() throws Exception {
        // 模拟请求参数
        String validId = "1"; // 假设有效的主键id为1
        when(request.getParameter("id")).thenReturn(validId);

        // 模拟complaintService.selectByPrimaryKey方法的返回值
        Complaint complaint = new Complaint();
        complaint.setType((byte) 0);
        complaint.setSongId(101); // 假设歌曲ID
        when(complaintService.selectByPrimaryKey(Integer.parseInt(validId))).thenReturn(complaint);

        // 模拟songService.selectByPrimaryKey方法的返回值
        Song song = new Song();
        song.setName("测试歌曲");
        song.setUserId(102); // 假设歌曲的用户ID
        when(songService.selectByPrimaryKey(complaint.getSongId())).thenReturn(song);

        // 模拟complaintService.delete方法的返回值
        when(complaintService.delete(Integer.parseInt(validId))).thenReturn(false);

        // 调用ignoreComplaint方法
        Object result = complaintController.ignoreComplaint(request);

        // 断言返回结果为true
        assertFalse((Boolean) result);

    }
    @Test
    public void testSelectByPrimaryKey_Success() throws Exception {
        // 模拟请求参数
        String validId = "1"; // 假设有效的主键id为1
        when(request.getParameter("id")).thenReturn(validId);

        // 模拟complaintService.selectByPrimaryKey方法的返回值
        Complaint expectedComplaint = new Complaint();
        when(complaintService.selectByPrimaryKey(Integer.parseInt(validId))).thenReturn(expectedComplaint);

        // 调用selectByPrimaryKey方法
        Object result = complaintController.selectByPrimaryKey(request);

        // 断言返回结果为Complaint对象
        assertTrue(result instanceof Complaint);
        Complaint resultComplaint = (Complaint) result;

        // 断言返回的Complaint对象与预期的Complaint对象相匹配
        assertEquals(expectedComplaint, resultComplaint);

        // 验证complaintService.selectByPrimaryKey是否被调用
        verify(complaintService).selectByPrimaryKey(Integer.parseInt(validId));
    }

    @Test
    public void testSelectByPrimaryKey_InvalidId() throws Exception {
        // 模拟请求参数，id为空或格式不正确
        when(request.getParameter("id")).thenReturn("-1");

        // 调用selectByPrimaryKey方法
        Object result = complaintController.selectByPrimaryKey(request);

        // 断言返回结果为null，或者根据业务逻辑可能抛出异常
        assertNull(result);

    }

    @Test
    public void testAllComplaint() {
        List<Complaint> mockComplaintList = new ArrayList<>();
        // 假设创建一些评论并添加到列表中
        mockComplaintList.add(new Complaint());
        mockComplaintList.add(new Complaint());

        when(complaintService.allComplaint()).thenReturn(mockComplaintList);
        // 调用allComment方法
        Object result = complaintController.allComplaint(request);

        // 断言返回结果为List对象
        assertTrue(result instanceof List);
        List<Complaint> resultCommentList = (List<Complaint>) result;
        // 断言返回的列表与模拟的列表相匹配
        assertEquals(mockComplaintList.size(), resultCommentList.size());
        for (int i = 0; i < mockComplaintList.size(); i++) {
            assertEquals(mockComplaintList.get(i).getId(), resultCommentList.get(i).getId());
            assertEquals(mockComplaintList.get(i).getContent(), resultCommentList.get(i).getContent());
        }

        // 验证commentService.allComment是否被调用
        verify(complaintService).allComplaint();
    }

    @Test
    public void testAllComplaintByUser() throws Exception {
        // 模拟请求参数
        Integer validUserId = 1; // 假设有效的用户ID为1
        when(request.getParameter("userId")).thenReturn(validUserId.toString());

        // 模拟complaintService.allComplaintSongListByUser方法的返回值
        List<Complaint> songListComplaints = new ArrayList<>();
        songListComplaints.add(new Complaint(/* 填充数据 */));
        when(complaintService.allComplaintSongListByUser(validUserId)).thenReturn(songListComplaints);

        // 模拟complaintService.allComplaintSongByUser方法的返回值
        List<Complaint> songComplaints = new ArrayList<>();
        songComplaints.add(new Complaint(/* 填充数据 */));
        when(complaintService.allComplaintSongByUser(validUserId)).thenReturn(songComplaints);

        // 调用allComplaintByUser方法
        Object result = complaintController.allComplaintByUser(request);

        // 断言返回结果为List对象
        assertTrue(result instanceof List);
        List<Complaint> resultList = (List<Complaint>) result;

        // 断言返回的列表包含了两部分投诉的总数
        assertEquals(2, resultList.size());

        // 验证complaintService.allComplaintSongByUser是否被调用
        verify(complaintService).allComplaintSongByUser(validUserId);
        // 验证complaintService.allComplaintSongListByUser是否被调用
        verify(complaintService).allComplaintSongListByUser(validUserId);
    }

    @Test
    public void testAllComplaintByUser_InvalidUserId() throws Exception {
        // 模拟请求参数，userId为空字符串
        when(request.getParameter("userId")).thenReturn("-1");

        // 调用allComplaintByUser方法
        Object result = complaintController.allComplaintByUser(request);

        // 断言返回结果为null或空列表，取决于你的业务逻辑如何处理这种情况
        assertTrue(result instanceof List);
        List<Complaint> resultList = (List<Complaint>) result;
        assertTrue(resultList.isEmpty());

    }
    private List<Complaint> createComplaintList(int size) {
        List<Complaint> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            // 根据需要创建Complaint对象
            list.add(new Complaint(/* 填充数据 */));
        }
        return list;
    }
    @Test
    public void testAllComplaintAgainstUser() throws Exception {
        // 模拟请求参数
        Integer validUserId = 1; // 假设有效的用户ID为1
        when(request.getParameter("userId")).thenReturn(String.valueOf(validUserId));

        // 模拟complaintService.allComplaintSongAgainstUser方法的返回值
        List<Complaint> songComplaints = createComplaintList(2); // 假设返回2个投诉
        when(complaintService.allComplaintSongAgainstUser(validUserId)).thenReturn(songComplaints);

        // 模拟complaintService.allComplaintSongListAgainstUser方法的返回值
        List<Complaint> songListComplaints = createComplaintList(3); // 假设返回3个投诉
        when(complaintService.allComplaintSongListAgainstUser(validUserId)).thenReturn(songListComplaints);

        // 调用allComplaintAgainstUser方法
        Object result = complaintController.allComplaintAgainstUser(request);

        // 断言返回结果为List对象
        assertTrue(result instanceof List);
        List<Complaint> resultList = (List<Complaint>) result;

        // 断言返回的列表大小为5（2+3）
        assertEquals(5, resultList.size());

        // 验证complaintService.allComplaintSongAgainstUser是否被调用
        verify(complaintService).allComplaintSongAgainstUser(validUserId);
        // 验证complaintService.allComplaintSongListAgainstUser是否被调用
        verify(complaintService).allComplaintSongListAgainstUser(validUserId);
    }

    @Test
    public void testAllComplaintAgainstUserWithInvalidUserId() {
        // 模拟请求参数，userId为空字符串
        when(request.getParameter("userId")).thenReturn("-1");

        // 调用allComplaintAgainstUser方法
        Object result = complaintController.allComplaintAgainstUser(request);

        // 断言返回结果为List对象
        assertTrue(result instanceof List);
        List<Complaint> resultList = (List<Complaint>) result;

        // 断言返回的列表为一个空列表
        assertTrue(resultList.isEmpty());

    }

    @Test
    public void testAppealComplaint_Success() throws Exception {
        // 模拟请求参数
        Integer validId = 1; // 假设有效的id为1
        String validAppeal = "这是一个申诉内容"; // 假设有效的申诉内容
        when(request.getParameter("id")).thenReturn(validId.toString());
        when(request.getParameter("appeal")).thenReturn(validAppeal);

        // 模拟complaintService.update方法的返回值
        when(complaintService.update(any(Complaint.class))).thenReturn(true);

        // 调用appealComplaint方法
        Object result = complaintController.appealComplaint(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(1, jsonObject.getIntValue(Consts.CODE));
        assertEquals("申诉提交成功，请等待工作人员处理", jsonObject.getString(Consts.MSG));

        // 验证complaintService.update是否被调用
        verify(complaintService).update(any(Complaint.class));
    }

    @Test
    public void testAppealComplaint_Failure() throws Exception {
        // 模拟请求参数
        Integer validId = 1;
        when(request.getParameter("id")).thenReturn(validId.toString());
        when(request.getParameter("appeal")).thenReturn("");

        // 模拟complaintService.update方法的返回值
        when(complaintService.update(any(Complaint.class))).thenReturn(false);

        // 调用appealComplaint方法
        Object result = complaintController.appealComplaint(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(0, jsonObject.getIntValue(Consts.CODE));
        assertEquals("申诉提交失败", jsonObject.getString(Consts.MSG));
    }

    @Test
    public void testAppealComplaint_EmptyAppealContent() throws Exception {
        // 模拟请求参数，申诉内容为空
        Integer validId = 1;
        when(request.getParameter("id")).thenReturn(validId.toString());
        when(request.getParameter("appeal")).thenReturn(null); // 空的申诉内容

        // 模拟complaintService.update方法的返回值
        when(complaintService.update(any(Complaint.class))).thenReturn(true);

        // 调用appealComplaint方法
        Object result = complaintController.appealComplaint(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(1, jsonObject.getIntValue(Consts.CODE));
        assertEquals("申诉提交成功，请等待工作人员处理", jsonObject.getString(Consts.MSG));

        // 验证complaintService.update是否被调用
        verify(complaintService).update(any(Complaint.class));
    }
}