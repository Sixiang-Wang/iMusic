package com.java.imusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.imusic.domain.SongList;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

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
        when(mockSongListService.selectByPrimaryKey(anyInt())).thenReturn(true);

        // 调用selectByPrimaryKey方法
        Object result = service.selectByPrimaryKey(request);

        // 验证结果
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;
        assertNotNull(jsonObject);
        assertEquals(1, jsonObject.getInteger("id"));
        assertEquals("Test Title", jsonObject.getString("title"));
    }

    @Test
    void selectByUserId() {
    }

    @Test
    void allSongList() {
    }

    @Test
    void songListOfTitle() {
    }

    @Test
    void likeTitle() {
    }

    @Test
    void likeStyle() {
    }

    @Test
    void updateSongListPic() {
    }

    @Test
    void bestSongListOfUser() {
    }

    @Test
    void invisibleSongList() {
    }

    @Test
    void allInvisibleSongList() {
    }

    @Test
    void visibleSongList() {
    }
}