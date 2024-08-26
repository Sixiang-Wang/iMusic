package com.java.imusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.imusic.dao.RankMapper;
import com.java.imusic.domain.Message;
import com.java.imusic.domain.Song;
import com.java.imusic.domain.SongList;
import com.java.imusic.service.MessageService;
import com.java.imusic.service.SongListService;
import com.java.imusic.service.UserService;
import com.java.imusic.utils.Consts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class SongListControllerTest {

    @InjectMocks
    private SongListController service;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private SongListService mockSongListService;

    @Mock
    private HttpSession session;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }
    @Test
    public void testAddSongListSuccess() throws Exception {
        // 设置请求参数
        when(request.getParameter("title")).thenReturn("Test Title");
        when(request.getParameter("introduction")).thenReturn("Test Introduction");
        when(request.getParameter("style")).thenReturn("Test Style");
        when(request.getParameter("userId")).thenReturn("1");

        // 设置SongListService的模拟行为，模拟添加成功的情况
        when(mockSongListService.insert(any(SongList.class))).thenReturn(true);

        // 调用addSongList方法
        Object result = service.addSongList(request);

        // 验证结果
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;
        assertEquals(1, jsonObject.getInteger(Consts.CODE));
        assertEquals("添加成功", jsonObject.getString(Consts.MSG));
    }
    @Test
    public void testAddSongListFailure() throws Exception {
        // 设置请求参数
        when(request.getParameter("title")).thenReturn("Test Title");
        when(request.getParameter("introduction")).thenReturn("Test Introduction");
        when(request.getParameter("style")).thenReturn("Test Style");
        when(request.getParameter("userId")).thenReturn("1");

        // 设置SongListService的模拟行为，模拟添加失败的情况
        when(mockSongListService.insert(any(SongList.class))).thenReturn(false);

        // 调用addSongList方法
        Object result = service.addSongList(request);

        // 验证结果
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;
        assertEquals(0, jsonObject.getInteger(Consts.CODE));
        assertEquals("添加失败", jsonObject.getString(Consts.MSG));
    }

    @Test
    public void testUpdateSongListSuccess() throws Exception {
        // 设置请求参数
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("title")).thenReturn("Updated Title");
        when(request.getParameter("introduction")).thenReturn("Updated Introduction");
        when(request.getParameter("style")).thenReturn("Updated Style");

        // 设置SongListService的模拟行为，模拟更新成功的情况
        when(mockSongListService.update(any(SongList.class))).thenReturn(true);

        // 调用updateSongList方法
        Object result = service.updateSongList(request);

        // 验证结果
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;
        assertEquals(1, jsonObject.getInteger(Consts.CODE));
        assertEquals("修改成功", jsonObject.getString(Consts.MSG));
    }

    @Test
    public void testUpdateSongListFailure() throws Exception {
        // 设置请求参数
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("title")).thenReturn("Updated Title");
        when(request.getParameter("introduction")).thenReturn("Updated Introduction");
        when(request.getParameter("style")).thenReturn("Updated Style");

        // 设置SongListService的模拟行为，模拟更新失败的情况
        when(mockSongListService.update(any(SongList.class))).thenReturn(false);

        // 调用updateSongList方法
        Object result = service.updateSongList(request);

        // 验证结果
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;
        assertEquals(0, jsonObject.getInteger(Consts.CODE));
        assertEquals("修改失败", jsonObject.getString(Consts.MSG));
    }


    @Test
    public void testDeleteSongListSuccess() throws Exception {
        // 设置请求参数
        when(request.getParameter("id")).thenReturn("1");

        // 设置SongListService的模拟行为，模拟删除成功的情况
        when(mockSongListService.delete(anyInt())).thenReturn(true);

        // 调用deleteSongList方法
        Object result = service.deleteSongList(request);

        // 验证结果
        assertTrue((Boolean) result);
    }

    @Test
    public void testDeleteSongListFailure() throws Exception {
        // 设置请求参数
        when(request.getParameter("id")).thenReturn("1");

        // 设置SongListService的模拟行为，模拟删除失败的情况
        when(mockSongListService.delete(anyInt())).thenReturn(false);

        // 调用deleteSongList方法
        Object result = service.deleteSongList(request);

        // 验证结果
        assertFalse((boolean) result);
    }

    @Test
    public void testSelectByPrimaryKeySuccess() throws Exception {
        // 设置请求参数
        when(request.getParameter("id")).thenReturn("1");

        // 创建一个模拟的SongList对象
        SongList mockSongList = new SongList();
        mockSongList.setId(1);
        mockSongList.setTitle("Test Title");

        // 设置SongListService的模拟行为，模拟查询成功的情况
        when(mockSongListService.selectByPrimaryKey(1)).thenReturn(mockSongList);

        // 调用selectByPrimaryKey方法
        Object result = service.selectByPrimaryKey(request);

        SongList res = (SongList) result;
        // 验证结果
        assertNotNull(result);
        assertEquals(1, res.getId());
        assertEquals("Test Title", res.getTitle());
    }

    @Test
    public void testSelectByPrimaryKeyNotFound() throws Exception {
        // 设置请求参数
        when(request.getParameter("id")).thenReturn("1");

        // 设置SongListService的模拟行为，模拟查询不到的情况
        when(mockSongListService.selectByPrimaryKey(1)).thenReturn(null);

        // 调用selectByPrimaryKey方法
        Object result = service.selectByPrimaryKey(request);

        // 验证结果
        assertNull(result);
    }
    @Test
    public void testSelectByUserIdSuccess() throws Exception {
        // 设置请求参数
        when(request.getParameter("userId")).thenReturn("1");

        SongList s1 = new SongList();
        s1.setUserId(1);
        s1.setTitle("T1");
        SongList s2 = new SongList();
        s2.setUserId(2);
        s2.setTitle("T2");
        // 创建模拟的SongList列表
        List<SongList> mockSongLists = new ArrayList<>();
        mockSongLists.add(s1);
        mockSongLists.add(s2);

        // 设置SongListService的模拟行为，模拟查询成功的情况
        when(mockSongListService.selectByUserId(1)).thenReturn(mockSongLists);

        // 调用selectByUserId方法
        Object result = service.selectByUserId(request);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof List);
        List<SongList> resultSongLists = (List<SongList>) result;
        assertEquals(2, resultSongLists.size());
        assertEquals("T1", resultSongLists.get(0).getTitle());
    }

    @Test
    public void testLikeTitleSuccess() throws Exception {
        when(request.getParameter("title")).thenReturn("pop");

        SongList s1 = new SongList();
        s1.setUserId(1);
        s1.setStyle("pop");
        SongList s2 = new SongList();
        s2.setUserId(2);
        s2.setStyle("pop");
        // 创建模拟的SongList列表
        List<SongList> mockSongLists = new ArrayList<>();
        mockSongLists.add(s1);
        mockSongLists.add(s2);
        when(mockSongListService.likeTitle("%pop%")).thenReturn(mockSongLists);
        Object result = service.likeTitle(request);
        assertNotNull(result);
        assertTrue(result instanceof List);
        List<SongList> resultSongLists = (List<SongList>) result;
        assertEquals(2, resultSongLists.size());
        assertEquals("pop", resultSongLists.get(0).getStyle());
    }

    @Mock
    private RankMapper mockRankMapper;
    @Test
    public void testBestSongListOfUserWithBestList() throws Exception {
        // 设置请求参数
        when(request.getParameter("userId")).thenReturn("1");

        // 创建一个模拟的SongList对象
        SongList mockSongList = new SongList();
        mockSongList.setId(1);
        mockSongList.setTitle("Best Song List");

        // 设置RankMapper的模拟行为，模拟有最佳歌单的情况
        when(mockRankMapper.bestSongListOfUser(1)).thenReturn(mockSongList);

        // 调用bestSongListOfUser方法
        Object result = service.bestSongListOfUser(request);

        // 验证结果
        assertNotNull(result);
        assertEquals(mockSongList, result);
    }
    @Test
    public void testBestSongListOfUserWithEmptyList() throws Exception {
        // 设置请求参数
        when(request.getParameter("userId")).thenReturn("1");

        // 设置RankMapper的模拟行为，模拟没有最佳歌单的情况
        when(mockRankMapper.bestSongListOfUser(1)).thenReturn(null);

        // 设置SongListService的模拟行为，模拟没有查询到其他歌单的情况
        when(mockSongListService.selectByUserId(1)).thenReturn(Collections.emptyList());

        // 调用bestSongListOfUser方法
        Object result = service.bestSongListOfUser(request);

        // 验证结果
        assertNull(result);
    }

    @Mock
    private MessageService mockMessageService;
    @Test
    public void testInvisibleSongListSuccess() throws Exception {
        // 设置请求参数
        when(request.getParameter("id")).thenReturn("1");

        // 创建一个模拟的SongList对象
        SongList mockSongList = new SongList();
        mockSongList.setId(1);
        mockSongList.setTitle("Test Song List");
        mockSongList.setUserId(1);
        mockSongList.setVisible(1); // 假设可见性初始值为1

        // 设置SongListService的模拟行为，模拟查询和更新成功的情况
        when(mockSongListService.selectByPrimaryKey(1)).thenReturn(mockSongList);
        when(mockSongListService.update(any(SongList.class))).thenReturn(true);

        // 调用invisibleSongList方法
        Object result = service.invisibleSongList(request);

        // 验证结果
        assertTrue((Boolean) result);
        verify(mockSongListService).update(any(SongList.class));
        verify(mockMessageService).insert(any(Message.class));
    }
    @Test
    public void testInvisibleSongListUpdateFailed() throws Exception {
        // 设置请求参数
        when(request.getParameter("id")).thenReturn("1");

        // 创建一个模拟的SongList对象
        SongList mockSongList = new SongList();
        mockSongList.setId(1);
        mockSongList.setTitle("Test Song List");
        mockSongList.setUserId(1);
        mockSongList.setVisible(1); // 假设可见性初始值为1

        // 设置SongListService的模拟行为，模拟查询成功但更新失败的情况
        when(mockSongListService.selectByPrimaryKey(1)).thenReturn(mockSongList);
        when(mockSongListService.update(any(SongList.class))).thenReturn(false);

        // 调用invisibleSongList方法
        Object result = service.invisibleSongList(request);

        // 验证结果
        assertFalse((Boolean) result);
        verify(mockSongListService).update(any(SongList.class));
        verify(mockMessageService, never()).insert(any(Message.class)); // 验证没有插入消息
    }

    @Test
    public void testVisibleSongListSuccess() throws Exception {
        // 设置请求参数
        when(request.getParameter("id")).thenReturn("1");

        // 创建一个模拟的SongList对象
        SongList mockSongList = new SongList();
        mockSongList.setId(1);
        mockSongList.setTitle("Test Song List");
        mockSongList.setUserId(1);
        mockSongList.setVisible(0); // 假设可见性初始值为0

        // 设置SongListService的模拟行为，模拟查询和更新成功的情况
        when(mockSongListService.selectByPrimaryKey(1)).thenReturn(mockSongList);
        when(mockSongListService.update(any(SongList.class))).thenReturn(true);

        // 调用visibleSongList方法
        Object result = service.visibleSongList(request);

        // 验证结果
        assertTrue((Boolean) result);
        verify(mockSongListService).update(any(SongList.class));
        verify(mockMessageService).insert(any(Message.class));
    }
    @Test
    public void testVisibleSongListUpdateFailed() throws Exception {
        // 设置请求参数
        when(request.getParameter("id")).thenReturn("1");

        // 创建一个模拟的SongList对象
        SongList mockSongList = new SongList();
        mockSongList.setId(1);
        mockSongList.setTitle("Test Song List");
        mockSongList.setUserId(1);
        mockSongList.setVisible(0); // 假设可见性初始值为0

        // 设置SongListService的模拟行为，模拟查询成功但更新失败的情况
        when(mockSongListService.selectByPrimaryKey(1)).thenReturn(mockSongList);
        when(mockSongListService.update(any(SongList.class))).thenReturn(false);

        // 调用visibleSongList方法
        Object result = service.visibleSongList(request);

        // 验证结果
        assertFalse((Boolean) result);
        verify(mockSongListService).update(any(SongList.class));
        verify(mockMessageService, never()).insert(any(Message.class)); // 验证没有插入消息
    }
}