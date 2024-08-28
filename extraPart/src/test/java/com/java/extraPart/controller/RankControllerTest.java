package com.java.extraPart.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.extraPart.domain.Rank;
import com.java.extraPart.service.RankService;
import com.java.extraPart.utils.Consts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RankControllerTest {

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private RankService mockRankService;

    @InjectMocks
    private RankController service;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }

    @Test
    public void testAddRankSuccess() {
        // 设置请求参数
        when(mockRequest.getParameter("id")).thenReturn("1");
        when(mockRequest.getParameter("songListId")).thenReturn("1");
        when(mockRequest.getParameter("userId")).thenReturn("1");
        when(mockRequest.getParameter("score")).thenReturn("5");

        // 创建一个模拟的Rank对象
        Rank mockRank = new Rank();
        mockRank.setId(1);
        mockRank.setSongListId(1);
        mockRank.setUserId(1);
        mockRank.setScore(5);

        // 设置RankService的模拟行为，模拟插入成功的情况
        when(mockRankService.insert(any(Rank.class))).thenReturn(true);

        // 调用add方法
        Object result = service.add(mockRequest);

        // 验证结果
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;
        assertEquals(1, jsonObject.getInteger(Consts.CODE));
        assertEquals("评价成功", jsonObject.getString(Consts.MSG));
    }
    @Test
    public void testAddRankFailure() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("songListId")).thenReturn("1");
        when(mockRequest.getParameter("userId")).thenReturn("1");
        when(mockRequest.getParameter("score")).thenReturn("5");

        // 设置RankService的模拟行为，模拟插入失败的情况
        when(mockRankService.insert(any(Rank.class))).thenReturn(false);

        // 调用add方法
        Object result = service.add(mockRequest);

        // 验证结果
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;
        assertEquals(0, jsonObject.getInteger(Consts.CODE));
        assertEquals("评价失败", jsonObject.getString(Consts.MSG));
    }
    @Test
    public void testUpdateRankSuccess() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("id")).thenReturn("1");
        when(mockRequest.getParameter("songListId")).thenReturn("1");
        when(mockRequest.getParameter("userId")).thenReturn("1");
        when(mockRequest.getParameter("score")).thenReturn("5");

        // 创建一个模拟的Rank对象
        Rank mockRank = new Rank();
        mockRank.setId(1);
        mockRank.setSongListId(1);
        mockRank.setUserId(1);
        mockRank.setScore(5);

        // 设置RankService的模拟行为，模拟查询到Rank对象
        when(mockRankService.getRank(any(Rank.class))).thenReturn(mockRank);
        // 设置RankService的模拟行为，模拟更新成功的情况
        when(mockRankService.update(any(Rank.class))).thenReturn(true);

        // 调用add方法
        Object result = service.add(mockRequest);

        // 验证结果
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;
        assertEquals(1, jsonObject.getInteger(Consts.CODE));
        assertEquals("评价更新成功", jsonObject.getString(Consts.MSG));
    }
    @Test
    public void testUpdateRankFailure() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("id")).thenReturn("1");
        when(mockRequest.getParameter("songListId")).thenReturn("1");
        when(mockRequest.getParameter("userId")).thenReturn("1");
        when(mockRequest.getParameter("score")).thenReturn("5");

        // 创建一个模拟的Rank对象
        Rank mockRank = new Rank();
        mockRank.setId(1);
        mockRank.setSongListId(1);
        mockRank.setUserId(1);
        mockRank.setScore(5);

        // 设置RankService的模拟行为，模拟查询到Rank对象
        when(mockRankService.getRank(any(Rank.class))).thenReturn(mockRank);
        // 设置RankService的模拟行为，模拟更新失败的情况
        when(mockRankService.update(any(Rank.class))).thenReturn(false);

        // 调用add方法
        Object result = service.add(mockRequest);

        // 验证结果
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;
        assertEquals(0, jsonObject.getInteger(Consts.CODE));
        assertEquals("评价更新失败", jsonObject.getString(Consts.MSG));
    }

    @Test
    public void testRankOfSongListIdAndUserIdFound() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("songListId")).thenReturn("1");
        when(mockRequest.getParameter("userId")).thenReturn("1");

        // 创建一个模拟的Rank对象
        Rank mockRank = new Rank();
        mockRank.setSongListId(1);
        mockRank.setUserId(1);
        mockRank.setScore(5);

        // 设置RankService的模拟行为，模拟查询到Rank对象的情况
        when(mockRankService.getRank(any(Rank.class))).thenReturn(mockRank);

        // 调用rankOfSongListIdAndUserId方法
        Object result = service.rankOfSongListIdAndUserId(mockRequest);

        // 验证结果
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;
        assertEquals(1, jsonObject.getInteger(Consts.CODE));
        assertEquals("查询成功", jsonObject.getString(Consts.MSG));
        assertEquals(5, jsonObject.getInteger("rank"));
    }
    @Test
    public void testRankOfSongListIdAndUserIdNotFound() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("songListId")).thenReturn("1");
        when(mockRequest.getParameter("userId")).thenReturn("1");

        // 设置RankService的模拟行为，模拟未查询到Rank对象的情况
        when(mockRankService.getRank(any(Rank.class))).thenReturn(null);

        // 调用rankOfSongListIdAndUserId方法
        Object result = service.rankOfSongListIdAndUserId(mockRequest);

        // 验证结果
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;
        assertEquals(0, jsonObject.getInteger(Consts.CODE));
        assertEquals("无评分", jsonObject.getString(Consts.MSG));
    }


    @Test
    public void testRankOfSongListIdSuccess() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("songListId")).thenReturn("1");

        Rank rank = new Rank();
        rank.setSongListId(1);
        rank.setScore(5);


        // 设置RankService的模拟行为，模拟查询成功的情况
        when(mockRankService.rankOfSongListId(1)).thenReturn(rank.getScore());

        // 调用rankOfSongListId方法
        Object result = service.rankOfSongListId(mockRequest);

        // 验证结果
        assertNotNull(result);
        assertEquals(5, rank.getScore());
        assertEquals(1, rank.getSongListId());
    }
    @Test
    public void testRankOfSongListIdNotFound() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("songListId")).thenReturn("1");

        // 设置RankService的模拟行为，模拟查询不到的情况
        when(mockRankService.rankOfSongListId(1)).thenReturn(-1);

        // 调用rankOfSongListId方法
        Object result = service.rankOfSongListId(mockRequest);

        // 验证结果
        assertEquals(-1,result);
    }
}