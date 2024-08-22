package com.java.imusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.imusic.domain.Collect;
import com.java.imusic.service.CollectService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CollectControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletResponse response;

    @Mock
    private CollectService collectService;

    @InjectMocks
    private CollectController collectController;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }

    // 辅助方法，用于提供预期的收藏列表
    private List<Collect> getExpectedCollectList() {
        // 这里返回你预期的收藏列表，例如：
        return new ArrayList<Collect>() {{
            add(new Collect()); // 假设Collect是收藏项的类
            // 可以添加更多的收藏项
        }};
    }

    @Test
    public void testAddCollectSuccess() {
        // 模拟请求参数
        when(request.getParameter("userId")).thenReturn("1");
        when(request.getParameter("type")).thenReturn("0");
        when(request.getParameter("songId")).thenReturn("1001");

        // 模拟服务层方法调用
        when(collectService.existSongId(1, 1001)).thenReturn(false);
        when(collectService.insert(any(Collect.class))).thenReturn(true);

        // 执行测试方法
        JSONObject result = (JSONObject) collectController.addCollect(request);

        // 验证结果
        assertEquals(1, result.getIntValue(Consts.CODE));
        assertEquals("收藏成功", result.getString(Consts.MSG));
    }

    @Test
    public void testAddCollectFailure() {
        // 模拟请求参数
        when(request.getParameter("userId")).thenReturn("1");
        when(request.getParameter("type")).thenReturn("0");
        when(request.getParameter("songId")).thenReturn("");

        // 调用addCollect方法
        JSONObject result = (JSONObject) collectController.addCollect(request);

        // 断言返回结果
        assertEquals(0, result.getIntValue(Consts.CODE));
        assertEquals("收藏失败", result.getString(Consts.MSG));
    }

    @Test
    public void testAddCollectAlreadyCollected() {
        // 模拟请求参数
        when(request.getParameter("userId")).thenReturn("1");
        when(request.getParameter("type")).thenReturn("0");
        when(request.getParameter("songId")).thenReturn("1001");

        // 模拟服务层方法调用，表示歌曲已经被收藏
        when(collectService.existSongId(1, 1001)).thenReturn(true);

        // 调用addCollect方法
        JSONObject result = (JSONObject) collectController.addCollect(request);

        // 断言返回结果
        assertEquals(2, result.getIntValue(Consts.CODE));
        assertEquals("已收藏", result.getString(Consts.MSG));
    }

    @Test
    public void testDeleteCollectSongSuccess() {
        // 模拟请求参数
        when(request.getParameter("userId")).thenReturn("1");
        when(request.getParameter("songId")).thenReturn("1001");

        // 模拟服务层方法调用，表示删除成功
        when(collectService.deleteByUserIdSongId(1, 1001)).thenReturn(true);

        // 调用deleteCollectSong方法
        boolean result = (boolean) collectController.deleteCollectSong(request);

        // 断言返回结果
        assertTrue(result);
    }

    @Test
    public void testDeleteCollectSongFailure() {
        // 模拟请求参数
        when(request.getParameter("userId")).thenReturn("1");
        when(request.getParameter("songId")).thenReturn("1001");

        // 模拟服务层方法调用，表示删除失败
        when(collectService.deleteByUserIdSongId(1, 1001)).thenReturn(false);

        // 调用deleteCollectSong方法
        boolean result = (boolean) collectController.deleteCollectSong(request);

        // 断言返回结果
        assertFalse(result);
    }

    @Test
    void testDeleteCollectSongListSuccess() {
        // 模拟请求参数
        when(request.getParameter("userId")).thenReturn("1");
        when(request.getParameter("songListId")).thenReturn("1001");

        // 模拟服务层方法调用，表示删除成功
        when(collectService.deleteByUserIdSongListId(1, 1001)).thenReturn(true);

        // 调用deleteCollectSong方法
        boolean result = (boolean) collectController.deleteCollectSongList(request);

        // 断言返回结果
        assertTrue(result);
    }

    @Test
    void testDeleteCollectSongListFailure() {
        // 模拟请求参数
        when(request.getParameter("userId")).thenReturn("1");
        when(request.getParameter("songListId")).thenReturn("1001");

        // 模拟服务层方法调用，表示删除成功
        when(collectService.deleteByUserIdSongListId(1, 1001)).thenReturn(false);

        // 调用deleteCollectSong方法
        boolean result = (boolean) collectController.deleteCollectSongList(request);

        // 断言返回结果
        assertFalse(result);
    }

    @Test
    public void testAllCollect() {
        // 模拟collectService.allCollect方法的返回值
        when(collectService.allCollect()).thenReturn(getExpectedCollectList());

        // 调用allCollect方法
        Object result = collectController.allCollect(request);

        // 断言返回结果，这里假设返回的是一个集合
        assertTrue(result instanceof List);
        List<?> collectList = (List<?>) result;
        // 这里可以根据你的预期结果添加更多的断言
        assertEquals(getExpectedCollectList().size(), collectList.size());
    }

    @Test
    public void testCollectOfUserId() {
        // 模拟请求参数
        when(request.getParameter("userId")).thenReturn("123"); // 假设userId为123

        // 模拟collectService.collectOfUserId方法的返回值
        when(collectService.collectOfUserId(123)).thenReturn(getExpectedCollectList());

        // 调用collectOfUserId方法
        Object result = collectController.collectOfUserId(request);

        // 断言返回结果，这里假设返回的是一个集合
        assertTrue(result instanceof List);
        List<?> collectList = (List<?>) result;
        // 这里可以根据你的预期结果添加更多的断言
        assertEquals(getExpectedCollectList().size(), collectList.size());
    }


    @Test
    public void testCollectSongOfUserId_ValidUserId() {
        // 模拟请求参数
        String validUserId = "1"; // 假设有效的userId为1
        when(request.getParameter("userId")).thenReturn(validUserId);

        // 模拟collectService.collectOfUserId方法的返回值
        List<Collect> mockCollectList = new ArrayList<>();
        when(collectService.collectOfUserId(Integer.parseInt(validUserId))).thenReturn(mockCollectList);

        // 调用collectSongOfUserId方法
        Object result = collectController.collectSongOfUserId(request);

        // 断言返回结果，这里假设返回的是一个Song列表
        assertTrue(result instanceof List);
        List<?> resultList = (List<?>) result;
        assertEquals(mockCollectList.size(), resultList.size()); // 假设所有收藏都是歌曲类型

        // 验证collectService.collectOfUserId是否被调用
        verify(collectService).collectOfUserId(Integer.parseInt(validUserId));
    }

    @Test
    public void testCollectSongOfUserId_InvalidUserId() {
        // 模拟请求参数，userId为空或无效
        when(request.getParameter("userId")).thenReturn("");

        // 调用collectSongOfUserId方法
        Object result = collectController.collectSongOfUserId(request);

        // 断言返回结果为null
        assertNull(result);
    }

    @Test
    public void testCollectSongListOfUserId_ValidUserId() {
        // 模拟请求参数
        String validUserId = "1"; // 假设有效的userId为1
        when(request.getParameter("userId")).thenReturn(validUserId);

        // 模拟collectService.collectOfUserId方法的返回值
        List<Collect> mockCollectList = new ArrayList<>();
        when(collectService.collectOfUserId(Integer.parseInt(validUserId))).thenReturn(mockCollectList);

        // 调用collectSongOfUserId方法
        Object result = collectController.collectSongListOfUserId(request);

        // 断言返回结果，这里假设返回的是一个Song列表
        assertTrue(result instanceof List);
        List<?> resultList = (List<?>) result;
        assertEquals(mockCollectList.size(), resultList.size()); // 假设所有收藏都是歌曲类型

        // 验证collectService.collectOfUserId是否被调用
        verify(collectService).collectOfUserId(Integer.parseInt(validUserId));
    }
    @Test
    public void testCollectSongListOfUserId_InvalidUserId() {
        // 模拟请求参数，userId为空或无效
        when(request.getParameter("userId")).thenReturn("");

        // 调用collectSongOfUserId方法
        Object result = collectController.collectSongListOfUserId(request);

        // 断言返回结果为null
        assertNull(result);
    }

    @Test
    public void testSongCollectNum_ValidSongId() {
        // 模拟请求参数
        String validSongId = "1"; // 假设有效的songId为1
        when(request.getParameter("songId")).thenReturn(validSongId);

        // 模拟collectService.songCollectNum方法的返回值
        int expectedCollectNum = 10; // 假设预期的收藏数量为10
        when(collectService.songCollectNum(Integer.parseInt(validSongId))).thenReturn(expectedCollectNum);

        // 调用songCollectNum方法
        Object result = collectController.songCollectNum(request);

        // 断言返回结果为预期的收藏数量
        assertEquals(expectedCollectNum, result);
    }

    @Test
    public void testSongCollectNum_InvalidSongId() {
        // 模拟请求参数，songId为空或无效
        when(request.getParameter("songId")).thenReturn("");

        // 调用songCollectNum方法
        Object result = collectController.songCollectNum(request);

        // 断言返回结果为-1
        assertEquals(-1, result);
    }


    @Test
    public void testSongListCollectNum_ValidId() {
        // 模拟请求参数
        String validSongListId = "1"; // 假设有效的songListId为1
        when(request.getParameter("songListId")).thenReturn(validSongListId);

        // 模拟collectService.songListCollectNum方法的返回值
        int expectedCollectNum = 5; // 假设预期的收藏数量为5
        when(collectService.songListCollectNum(Integer.parseInt(validSongListId))).thenReturn(expectedCollectNum);

        // 调用songListCollectNum方法
        Object result = collectController.songListCollectNum(request);

        // 断言返回结果为预期的收藏数量
        assertEquals(expectedCollectNum, result);
    }

    @Test
    public void testSongListCollectNum_InvalidId() {
        // 模拟请求参数，songListId为空字符串
        when(request.getParameter("songListId")).thenReturn("");

        // 调用songListCollectNum方法
        Object result = collectController.songListCollectNum(request);

        // 断言返回结果为-1
        assertEquals(-1, result);
    }

    @Test
    public void testExistCollectSong_Exists() {
        // 模拟请求参数
        Integer validUserId = 1; // 假设有效的userId为1
        Integer validSongId = 100; // 假设有效的songId为100
        when(request.getParameter("userId")).thenReturn(validUserId.toString());
        when(request.getParameter("songId")).thenReturn(validSongId.toString());

        // 模拟collectService.existSongId方法的返回值
        boolean expectedExists = true; // 假设预期的结果是存在
        when(collectService.existSongId(validUserId, validSongId)).thenReturn(expectedExists);

        // 调用existCollectSong方法
        Object result = collectController.existCollectSong(request);

        // 断言返回结果为预期的布尔值
        assertEquals(expectedExists, result);
    }

    @Test
    public void testExistCollectSong_NotExists() {
        // 使用不同的参数模拟不存在的情况
        Integer validUserId = 1;
        Integer validSongId = 999; // 假设songId为999时不存在
        when(request.getParameter("userId")).thenReturn(validUserId.toString());
        when(request.getParameter("songId")).thenReturn(validSongId.toString());

        // 模拟collectService.existSongId方法的返回值
        boolean expectedNotExists = false; // 假设预期的结果是不存在
        when(collectService.existSongId(validUserId, validSongId)).thenReturn(expectedNotExists);

        // 调用existCollectSong方法
        Object result = collectController.existCollectSong(request);

        // 断言返回结果为预期的布尔值
        assertEquals(expectedNotExists, result);
    }

    @Test
    public void testExistCollectSongList_Exists() {
        // 模拟请求参数
        Integer validUserId = 1; // 假设有效的userId为1
        Integer validSongListId = 100; // 假设有效的songListId为100
        when(request.getParameter("userId")).thenReturn(validUserId.toString());
        when(request.getParameter("songListId")).thenReturn(validSongListId.toString());

        // 模拟collectService.existSongListId方法的返回值
        boolean expectedExists = true; // 假设预期的结果是存在
        when(collectService.existSongListId(validUserId, validSongListId)).thenReturn(expectedExists);

        // 调用existCollectSongList方法
        Object result = collectController.existCollectSongList(request);

        // 断言返回结果为预期的布尔值
        assertEquals(expectedExists, result);
    }

    @Test
    public void testExistCollectSongList_NotExists() {
        // 使用不同的参数模拟不存在的情况
        Integer validUserId = 1;
        Integer validSongListId = 999; // 假设songListId为999时不存在
        when(request.getParameter("userId")).thenReturn(validUserId.toString());
        when(request.getParameter("songListId")).thenReturn(validSongListId.toString());

        // 模拟collectService.existSongListId方法的返回值
        boolean expectedNotExists = false; // 假设预期的结果是不存在
        when(collectService.existSongListId(validUserId, validSongListId)).thenReturn(expectedNotExists);

        // 调用existCollectSongList方法
        Object result = collectController.existCollectSongList(request);

        // 断言返回结果为预期的布尔值
        assertEquals(expectedNotExists, result);
    }
}