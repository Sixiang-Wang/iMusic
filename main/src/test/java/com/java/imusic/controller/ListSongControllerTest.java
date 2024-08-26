package com.java.imusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.imusic.domain.*;
import com.java.imusic.service.*;
import com.java.imusic.utils.Consts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ListSongControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ListSongService listSongService;

    @Mock
    private SongService songService;

    @InjectMocks
    private ListSongController listSongController;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }

    @Test
    public void testAddListSong_Success() throws Exception {
        // 模拟请求参数
        String validSongId = "1"; // 假设有效的歌曲ID为1
        String validSongListId = "2"; // 假设有效的歌单ID为2
        when(request.getParameter("songId")).thenReturn(validSongId);
        when(request.getParameter("songListId")).thenReturn(validSongListId);

        // 模拟listSongService.insert方法的返回值
        when(listSongService.insert(any(ListSong.class))).thenReturn(true);

        // 调用addListSong方法
        Object result = listSongController.addListSong(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(1, jsonObject.getIntValue(Consts.CODE));
        assertEquals("保存成功", jsonObject.getString(Consts.MSG));

        // 验证listSongService.insert是否被调用
        verify(listSongService).insert(any(ListSong.class));
    }

    @Test
    public void testAddListSong_Failure() throws Exception {
        // 模拟请求参数
        String validSongId = "1";
        String validSongListId = "2";
        when(request.getParameter("songId")).thenReturn(validSongId);
        when(request.getParameter("songListId")).thenReturn(validSongListId);

        // 模拟listSongService.insert方法的返回值
        when(listSongService.insert(any(ListSong.class))).thenReturn(false);

        // 调用addListSong方法
        Object result = listSongController.addListSong(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(0, jsonObject.getIntValue(Consts.CODE));
        assertEquals("保存失败", jsonObject.getString(Consts.MSG));
    }

    @Test
    public void testDetail_Success() throws Exception {
        // 模拟请求参数
        String validSongListId = "1"; // 假设有效的歌单ID为1
        when(request.getParameter("songListId")).thenReturn(validSongListId);

        // 模拟listSongService.listSongOfSongListId方法的返回值
        List<ListSong> expectedListSongs = Arrays.asList(new ListSong(/* 填充数据 */));
        when(listSongService.listSongOfSongListId(Integer.parseInt(validSongListId))).thenReturn(expectedListSongs);

        // 调用detail方法
        Object result = listSongController.detail(request);

        // 断言返回结果为List对象
        assertTrue(result instanceof List);
        List<?> resultList = (List<?>) result;

        // 断言返回的列表与预期的列表相匹配
        assertEquals(expectedListSongs, resultList);

        // 验证listSongService.listSongOfSongListId是否被调用
        verify(listSongService).listSongOfSongListId(Integer.parseInt(validSongListId));
    }

    @Test
    public void testDetail_InvalidSongListId() throws Exception {
        // 模拟请求参数，songListId为空字符串
        when(request.getParameter("songListId")).thenReturn("-1");

        // 调用detail方法
        Object result = listSongController.detail(request);

        // 断言返回结果为null或空列表，取决于你的业务逻辑如何处理这种情况
        assertTrue(result instanceof List);
        List<?> resultList = (List<?>) result;
        assertTrue(resultList.isEmpty());

    }

    @Test
    public void testAllSong_Success() throws Exception {
        // 模拟请求参数
        String validSongListId = "1"; // 假设有效的歌单ID为1
        when(request.getParameter("songListId")).thenReturn(validSongListId);

        // 模拟listSongService.listSongOfSongListId方法的返回值
        List<ListSong> listSongs = Arrays.asList(new ListSong(), new ListSong());
        when(listSongService.listSongOfSongListId(Integer.parseInt(validSongListId))).thenReturn(listSongs);

        // 模拟songService.selectByPrimaryKey方法的返回值
        when(songService.selectByPrimaryKey(anyInt())).thenReturn(new Song(/* 填充数据 */));

        // 调用allSong方法
        Object result = listSongController.allSong(request);

        // 断言返回结果为List对象
        assertTrue(result instanceof List);
        List<?> songs = (List<?>) result;

        // 断言返回的列表大小与模拟的listSongs大小相匹配
        assertEquals(listSongs.size(), songs.size());

        // 验证listSongService.listSongOfSongListId是否被调用
        verify(listSongService).listSongOfSongListId(Integer.parseInt(validSongListId));

    }

    @Test
    public void testAllSong_EmptyListSongs() throws Exception {
        // 模拟请求参数，返回空列表的情况
        when(request.getParameter("songListId")).thenReturn("1");

        // 模拟listSongService.listSongOfSongListId方法的返回值
        when(listSongService.listSongOfSongListId(anyInt())).thenReturn(new ArrayList<>());

        // 调用allSong方法
        Object result = listSongController.allSong(request);

        // 断言返回结果为List对象，且列表为空
        assertTrue(result instanceof List);
        List<?> songs = (List<?>) result;
        assertTrue(songs.isEmpty());
    }

    @Test
    public void testDelete_Success() throws Exception {
        // 模拟请求参数
        String validSongId = "1"; // 假设有效的歌曲ID为1
        String validSongListId = "2"; // 假设有效的歌单ID为2
        when(request.getParameter("songId")).thenReturn(validSongId);
        when(request.getParameter("songListId")).thenReturn(validSongListId);

        // 模拟listSongService.deleteBySongIdAndSongListId方法的返回值
        when(listSongService.deleteBySongIdAndSongListId(
                Integer.parseInt(validSongId),
                Integer.parseInt(validSongListId))).thenReturn(true);

        // 调用delete方法
        Object result = listSongController.delete(request);

        // 断言返回结果为true
        assertTrue((Boolean) result);

        // 验证listSongService.deleteBySongIdAndSongListId是否被调用
        verify(listSongService).deleteBySongIdAndSongListId(
                Integer.parseInt(validSongId),
                Integer.parseInt(validSongListId));
    }

    @Test
    public void testDelete_Failure() throws Exception {
        // 模拟请求参数
        String validSongId = "1";
        String validSongListId = "2";
        when(request.getParameter("songId")).thenReturn(validSongId);
        when(request.getParameter("songListId")).thenReturn(validSongListId);

        // 模拟listSongService.deleteBySongIdAndSongListId方法的返回值
        when(listSongService.deleteBySongIdAndSongListId(
                Integer.parseInt(validSongId),
                Integer.parseInt(validSongListId))).thenReturn(false);

        // 调用delete方法
        Object result = listSongController.delete(request);

        // 断言返回结果为false
        assertFalse((Boolean) result);
    }
}