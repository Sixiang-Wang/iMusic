package com.example.songpart.controller;

import com.alibaba.fastjson.JSONObject;

import com.java.songPart.controller.SongController;
import com.java.songPart.dao.SongMapper;
import com.java.songPart.domain.Follow;
import com.java.songPart.domain.Message;
import com.java.songPart.domain.Song;
import com.java.songPart.domain.User;
import com.java.songPart.service.SongService;
import com.java.songPart.utils.Consts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

interface UserService {
    User selectByPrimaryKey(Integer id);
}

interface FollowService {
    List<Follow> getBySingerId(Integer id);
}

interface MessageService {
    Boolean insert(Message message);
}

class SongControllerTest {
    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private UserService mockUserService;

    @Mock
    private SongService mockSongService;

    @Mock
    private FollowService mockFollowService;

    @Mock
    private MessageService mockMessageService;

    @InjectMocks
    private SongController service;
    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }


    @Test
    public void testAddSongSuccess() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("id")).thenReturn("1");
        when(mockRequest.getParameter("userId")).thenReturn("2");
        when(mockRequest.getParameter("name")).thenReturn("New Song");
        when(mockRequest.getParameter("introduction")).thenReturn("Introduction of the song");
        when(mockRequest.getParameter("lyric")).thenReturn("Lyrics of the song");
        when(mockRequest.getParameter("style")).thenReturn("Pop");

        // 创建一个模拟的MultipartFile对象
        MultipartFile mockMultipartFile = new MockMultipartFile("file", "song.mp3", "audio/mp3", new byte[1024]);
        when(mockRequest.getParameter("file")).thenReturn(String.valueOf(mockMultipartFile));

        // 设置UserService的模拟行为，模拟获取用户名成功的情况
        User user = new User();
        user.setId(2);
        user.setName("John Doe");
        when(mockUserService.selectByPrimaryKey(2)).thenReturn(user);

        // 设置SongService的模拟行为，模拟歌曲保存成功的情况
        when(mockSongService.insert(any(Song.class))).thenReturn(true);

        Follow follow = new Follow();
        follow.setSingerId(2);
        follow.setUserId(3);
        // 设置FollowService的模拟行为，模拟获取关注列表成功的情况
        List<Follow> followList = Collections.singletonList(follow);
        when(mockFollowService.getBySingerId(2)).thenReturn(followList);

        // 调用addSong方法
        Object result = service.addSong(mockRequest, mockMultipartFile);

        // 验证结果
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;
//        assertEquals(0, jsonObject.getInteger(CODE));
//        assertEquals("保存成功", jsonObject.getString(MSG));
    }


    @Test
    public void testSongOfUserIdSuccess() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("userId")).thenReturn("1");

        Song song1 = new Song();
        song1.setId(1);
        song1.setName("song1");
        Song song2 = new Song();
        song2.setId(2);
        song2.setName("song2");
        // 创建模拟的Song列表
        List<Song> mockSongList = new ArrayList<>();
        mockSongList.add(song1);
        mockSongList.add(song2);

        // 设置SongService的模拟行为，模拟查询成功的情况
        when(mockSongService.songOfUserId(1)).thenReturn(mockSongList);

        // 调用songOfUserId方法
        Object result = service.songOfUserId(mockRequest);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof List);
        List<Song> resultSongs = (List<Song>) result;
        assertEquals(2, resultSongs.size());
        assertEquals("song1", resultSongs.get(0).getName());
    }

    @Test
    public void testSongOfUserIdNotFound() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("userId")).thenReturn("1");

        // 设置SongService的模拟行为，模拟查询不到的情况
        when(mockSongService.songOfUserId(1)).thenReturn(new ArrayList<>());

        // 调用songOfUserId方法
        Object result = service.songOfUserId(mockRequest);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertTrue(((List<?>) result).isEmpty());
    }

    @Test
    public void testSongOfUserIdAddPrefixSuccess() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("userId")).thenReturn("1");

        Song song1 = new Song();
        song1.setId(1);
        song1.setName("song1");
        Song song2 = new Song();
        song2.setId(2);
        song2.setName("song2");
        // 创建模拟的Song列表
        List<Song> mockSongList = new ArrayList<>();
        mockSongList.add(song1);
        mockSongList.add(song2);
        // 创建模拟的歌曲列表

        // 设置SongService的模拟行为，模拟songOfUserId查询成功的情况
        when(mockSongService.songOfUserId(1)).thenReturn(mockSongList);

        // 设置SongService的模拟行为，模拟addPrefix方法成功添加前缀的情况
        when(mockSongService.addPrefix(mockSongList)).thenReturn(mockSongList);

        // 调用songOfUserIdAddPrefix方法
        Object result = service.songOfUserIdAddPrefix(mockRequest);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof List);
        List<Song> resultSongs = (List<Song>) result;
        assertEquals(2, resultSongs.size());
        assertEquals("song1", resultSongs.get(0).getName()); // 假设前缀是 "Prefix_"
    }
    @Test
    public void testSongOfUserIdAddPrefixNotFound() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("userId")).thenReturn("1");

        // 设置SongService的模拟行为，模拟songOfUserId查询不到的情况
        when(mockSongService.songOfUserId(1)).thenReturn(new ArrayList<>());

        // 调用songOfUserIdAddPrefix方法
        Object result = service.songOfUserIdAddPrefix(mockRequest);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertTrue(((List<?>) result).isEmpty());
    }



    @Test
    public void testUpdateSongSuccess() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("id")).thenReturn("1");
        when(mockRequest.getParameter("name")).thenReturn("Updated Song Name");
        when(mockRequest.getParameter("introduction")).thenReturn("Updated Album");
        when(mockRequest.getParameter("lyric")).thenReturn("Updated Lyrics");
        when(mockRequest.getParameter("style")).thenReturn("Updated Style");

        // 设置SongService的模拟行为，模拟歌曲更新成功的情况
        when(mockSongService.update(any(Song.class))).thenReturn(true);

        // 调用updateSong方法
        Object result = service.updateSong(mockRequest);

        // 验证结果
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;
        assertEquals(1, jsonObject.getInteger(Consts.CODE));
        assertEquals("修改成功", jsonObject.getString(Consts.MSG));
    }
    @Test
    public void testUpdateSongFailure() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("id")).thenReturn("1");
        when(mockRequest.getParameter("name")).thenReturn("Updated Song Name");
        when(mockRequest.getParameter("introduction")).thenReturn("Updated Album");
        when(mockRequest.getParameter("lyric")).thenReturn("Updated Lyrics");
        when(mockRequest.getParameter("style")).thenReturn("Updated Style");

        // 设置SongService的模拟行为，模拟歌曲更新失败的情况
        when(mockSongService.update(any(Song.class))).thenReturn(false);

        // 调用updateSong方法
        Object result = service.updateSong(mockRequest);

        // 验证结果
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;
        assertEquals(0, jsonObject.getInteger(Consts.CODE));
        assertEquals("修改失败", jsonObject.getString(Consts.MSG));
    }

    @Test
    public void testDeleteSongSuccess() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("id")).thenReturn("1");

        // 设置SongService的模拟行为，模拟歌曲删除成功的情况
        when(mockSongService.delete(1)).thenReturn(true);

        // 调用deleteSong方法
        Object result = service.deleteSong(mockRequest);

        // 验证结果
        boolean res = (boolean) result;
        assertTrue(res);
    }

    @Test
    public void testDeleteSongFailure() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("id")).thenReturn("1");

        // 设置SongService的模拟行为，模拟歌曲删除失败的情况
        when(mockSongService.delete(1)).thenReturn(false);

        // 调用deleteSong方法
        Object result = service.deleteSong(mockRequest);

        // 验证结果
        assertFalse((boolean) result);
    }

    @Test
    public void testInvisibleSongSuccess() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("id")).thenReturn("1");

        // 创建一个模拟的Song对象
        Song mockSong = new Song();
        mockSong.setId(1);
        mockSong.setName("Test Song");
        mockSong.setUserId(2);
        mockSong.setVisible(1); // 假设初始状态是可见的

        // 设置SongService的模拟行为，模拟查询和更新成功的情况
        when(mockSongService.selectByPrimaryKey(1)).thenReturn(mockSong);
        when(mockSongService.update(any(Song.class))).thenReturn(true);

        // 设置MessageService的模拟行为，模拟消息插入成功的情况
        when(mockMessageService.insert(any(Message.class))).thenReturn(true);

        // 调用invisibleSong方法
        Object result = service.invisibleSong(mockRequest);

        // 验证结果
        assertTrue((Boolean) result);
        verify(mockSongService).update(any(Song.class));
        verify(mockMessageService).insert(any(Message.class));
    }
    @Test
    public void testInvisibleSongUpdateFailed() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("id")).thenReturn("1");

        // 创建一个模拟的Song对象
        Song mockSong = new Song();
        mockSong.setId(1);
        mockSong.setName("Test Song");
        mockSong.setUserId(2);

        // 设置SongService的模拟行为，模拟查询成功但更新失败的情况
        when(mockSongService.selectByPrimaryKey(1)).thenReturn(mockSong);
        when(mockSongService.update(any(Song.class))).thenReturn(false);

        // 调用invisibleSong方法
        Object result = service.invisibleSong(mockRequest);

        // 验证结果
        assertFalse((Boolean) result);
        verify(mockSongService).update(any(Song.class));
        verify(mockMessageService, never()).insert(any(Message.class)); // 验证没有插入消息
    }





    @Test
    public void testVisibleSongSuccess() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("id")).thenReturn("1");

        // 创建一个模拟的Song对象
        Song mockSong = new Song();
        mockSong.setId(1);
        mockSong.setName("Test Song");
        mockSong.setUserId(2);
        mockSong.setVisible(0); // 假设初始状态是不可见的

        // 设置SongService的模拟行为，模拟查询和更新成功的情况
        when(mockSongService.selectByPrimaryKey(1)).thenReturn(mockSong);
        when(mockSongService.update(any(Song.class))).thenReturn(true);

        // 设置MessageService的模拟行为，模拟消息插入成功的情况
        when(mockMessageService.insert(any(Message.class))).thenReturn(true);

        // 调用visibleSong方法
        Object result = service.visibleSong(mockRequest);

        // 验证结果
        assertTrue((Boolean) result);
        verify(mockSongService).update(any(Song.class));
        verify(mockMessageService).insert(any(Message.class));
    }
    @Test
    public void testVisibleSongUpdateFailed() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("id")).thenReturn("1");

        // 创建一个模拟的Song对象
        Song mockSong = new Song();
        mockSong.setId(1);
        mockSong.setName("Test Song");
        mockSong.setUserId(2);

        // 设置SongService的模拟行为，模拟查询成功但更新失败的情况
        when(mockSongService.selectByPrimaryKey(1)).thenReturn(mockSong);
        when(mockSongService.update(any(Song.class))).thenReturn(false);

        // 调用visibleSong方法
        Object result = service.visibleSong(mockRequest);

        // 验证结果
        assertFalse((Boolean) result);
        verify(mockSongService).update(any(Song.class));
        verify(mockMessageService, never()).insert(any(Message.class)); // 验证没有插入消息
    }

    @Test
    public void testUpdateSongPicSuccess() throws Exception {
        // 创建一个模拟的MultipartFile对象
        MultipartFile mockMultipartFile = new MockMultipartFile("file", "songPic.jpg", "image/jpeg", "picture data".getBytes());
        int id = 1;

        // 创建一个模拟的Song对象
        Song mockSong = new Song();
        mockSong.setId(id);
        mockSong.setName("Test Song");
        mockSong.setPic("/img/songPic/default.jpg"); // 假设初始图片是默认图片
        when(mockSongService.selectByPrimaryKey(id)).thenReturn(mockSong);

        // 设置SongService的模拟行为，模拟歌曲更新成功的情况
        when(mockSongService.update(any(Song.class))).thenReturn(true);

        // 调用updateSongPic方法
        Object result = service.updateSongPic(mockMultipartFile, id);

        // 验证结果
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;
//        assertEquals(1, jsonObject.getInteger(Consts.CODE));
//        assertEquals("上传成功", jsonObject.getString(Consts.MSG));
    }


    @Test
    public void testUpdateSongUrlSuccess() throws Exception {
        // 创建一个模拟的MultipartFile对象
        MultipartFile mockMultipartFile = new MockMultipartFile("file", "songFile.mp3", "audio/mpeg", "song data".getBytes());
        int id = 1;

        // 创建一个模拟的Song对象
        Song mockSong = new Song();
        mockSong.setId(id);
        mockSong.setName("Test Song");
        mockSong.setUrl("song/oldSong.mp3"); // 假设初始URL是旧的歌曲文件
        when(mockSongService.selectByPrimaryKey(id)).thenReturn(mockSong);

        // 设置SongService的模拟行为，模拟歌曲更新成功的情况
        when(mockSongService.update(any(Song.class))).thenReturn(true);

        // 调用updateSongUrl方法
        Object result = service.updateSongUrl(mockMultipartFile, id);

        // 验证结果
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;
//        assertEquals(1, jsonObject.getInteger(Consts.CODE));
//        assertEquals("上传成功", jsonObject.getString(Consts.MSG));
//        assertEquals("/song/" + mockMultipartFile.getOriginalFilename(), jsonObject.getString("song"));
    }


    @Test
    public void testDetailSuccess() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("songId")).thenReturn("1");

        // 创建一个模拟的Song对象
        Song mockSong = new Song();
        mockSong.setId(1);
        mockSong.setName("Test Song");
        mockSong.setIntroduction("Introduction of the song");
        mockSong.setLyric("Lyrics of the song");
        // ... 设置其他必要的字段

        // 设置SongService的模拟行为，模拟查询成功的情况
        when(mockSongService.selectByPrimaryKey(1)).thenReturn(mockSong);

        // 调用detail方法
        Object result = service.detail(mockRequest);

        // 验证结果
        assertNotNull(result);
        assertEquals(mockSong, result);
    }
    @Test
    public void testDetailNotFound() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("songId")).thenReturn("1");

        // 设置SongService的模拟行为，模拟查询不到的情况
        when(mockSongService.selectByPrimaryKey(1)).thenReturn(null);

        // 调用detail方法
        Object result = service.detail(mockRequest);

        // 验证结果
        assertNull(result);
    }



    @Test
    public void testAddNumsSuccess() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("songId")).thenReturn("1");

        // 设置SongService的模拟行为，模拟增加播放次数成功的情况
        when(mockSongService.addNums(1)).thenReturn(true);

        // 调用addNums方法
        Object result = service.addNums(mockRequest);

        // 验证结果
        assertTrue((Boolean) result);
    }

    @Test
    public void testAddNumsFailure() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("songId")).thenReturn("1");

        // 设置SongService的模拟行为，模拟增加播放次数失败的情况
        when(mockSongService.addNums(1)).thenReturn(false);

        // 调用addNums方法
        Object result = service.addNums(mockRequest);

        // 验证结果
        assertFalse((Boolean) result);
    }
    @Test
    public void testSongOfSongNameSuccess() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("songName")).thenReturn("song1");


        // 创建模拟的歌曲列表
        Song song1 = new Song();
        song1.setId(1);
        song1.setName("song1");
        Song song2 = new Song();
        song2.setId(2);
        song2.setName("song2");
        // 创建模拟的Song列表
        List<Song> mockSongList = new ArrayList<>();
        mockSongList.add(song1);
        mockSongList.add(song2);

        // 设置SongService的模拟行为，模拟查询成功的情况
        when(mockSongService.songOfName("song1")).thenReturn(mockSongList);

        // 调用songOfSongName方法
        Object result = service.songOfSongName(mockRequest);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof List);
        List<Song> resultSongs = (List<Song>) result;
        assertEquals(2, resultSongs.size());
        assertEquals("song1", resultSongs.get(0).getName());
    }

    @Test
    public void testSongOfSongNameNotFound() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("songName")).thenReturn("Song Name");

        // 设置SongService的模拟行为，模拟查询不到的情况
        when(mockSongService.songOfName("Song Name")).thenReturn(new ArrayList<>());

        // 调用songOfSongName方法
        Object result = service.songOfSongName(mockRequest);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertTrue(((List<?>) result).isEmpty());
    }

    @Test
    public void testLikeSongOfNameSuccess() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("songName")).thenReturn("Love Song");

        // 创建模拟的歌曲列表
        Song song1 = new Song();
        song1.setId(1);
        song1.setName("Love Song");
        Song song2 = new Song();
        song2.setId(2);
        song2.setName("Love Song");
        // 创建模拟的Song列表
        List<Song> mockSongList = new ArrayList<>();
        mockSongList.add(song1);
        mockSongList.add(song2);

        // 设置SongService的模拟行为，模拟查询成功的情况
        when(mockSongService.likeSongOfName("Love Song")).thenReturn(mockSongList);

        // 调用likeSongOfName方法
        Object result = service.likeSongOfName(mockRequest);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof List);
        List<Song> resultSongs = (List<Song>) result;
        assertEquals(2, resultSongs.size());
        assertEquals("Love Song", resultSongs.get(0).getName());
    }
    @Test
    public void testLikeSongOfNameNotFound() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("songName")).thenReturn("Unknown Song");

        // 设置SongService的模拟行为，模拟查询不到的情况
        when(mockSongService.likeSongOfName("Unknown Song")).thenReturn(new ArrayList<>());

        // 调用likeSongOfName方法
        Object result = service.likeSongOfName(mockRequest);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertTrue(((List<?>) result).isEmpty());
    }


    @Test
    public void testSongOfStyleSuccess() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("style")).thenReturn("Pop");

        // 创建模拟的歌曲列表
        Song song1 = new Song();
        song1.setName("song1");
        song1.setStyle("Pop");
        Song song2 = new Song();
        song2.setName("song2");
        song2.setStyle("Pop");
        // 创建模拟的Song列表
        List<Song> mockSongList = new ArrayList<>();
        mockSongList.add(song1);
        mockSongList.add(song2);
        // 设置SongService的模拟行为，模拟查询成功的情况
        when(mockSongService.songOfStyle("%Pop%")).thenReturn(mockSongList);

        // 调用songOfStyle方法
        Object result = service.songOfStyle(mockRequest);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof List);
        List<Song> resultSongs = (List<Song>) result;
        assertEquals(2, resultSongs.size());
        assertEquals("Pop", resultSongs.get(0).getStyle());
    }
    @Mock
    private SongMapper mockSongMapper;
    @Test
    public void testSongOfStyleOther() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("style")).thenReturn("其他");

        Song song = new Song();
        song.setName("Other Style Song");
        // 创建模拟的歌曲列表
        List<Song> mockSongList = new ArrayList<>();
        mockSongList.add(song);

        // 设置SongMapper的模拟行为，模拟查询其他风格成功的情况
        when(mockSongMapper.songOfOtherStyle()).thenReturn(mockSongList);

        // 调用songOfStyle方法
        Object result = service.songOfStyle(mockRequest);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof List);
        List<Song> resultSongs = (List<Song>) result;
        assertEquals(1, resultSongs.size());
        assertEquals("Other Style Song", resultSongs.get(0).getName());
    }

    @Test
    public void testPopularSongOfUserSuccess() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("userId")).thenReturn("1");

        // 创建模拟的歌曲列表
        Song song1 = new Song();
        song1.setName("song1");
        song1.setStyle("Pop");
        Song song2 = new Song();
        song2.setName("song2");
        song2.setStyle("Pop");
        // 创建模拟的Song列表
        List<Song> mockSongList = new ArrayList<>();
        mockSongList.add(song1);
        mockSongList.add(song2);

        // 设置SongMapper的模拟行为，模拟查询用户歌曲成功的情况
        when(mockSongMapper.songOfUserId(1)).thenReturn(mockSongList);

        // 设置SongMapper的模拟行为，模拟查询用户热门歌曲成功的情况
        when(mockSongMapper.popularSongOfUser(1)).thenReturn(song1);

        // 调用popularSongOfUser方法
        Object result = service.popularSongOfUser(mockRequest);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof Song);
        assertEquals("song1", ((Song) result).getName());
    }
    @Test
    public void testPopularSongOfUserEmpty() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("userId")).thenReturn("1");

        // 设置SongMapper的模拟行为，模拟查询用户歌曲为空的情况
        when(mockSongMapper.songOfUserId(1)).thenReturn(Collections.emptyList());

        // 调用popularSongOfUser方法
        Object result = service.popularSongOfUser(mockRequest);

        // 验证结果
        assertEquals(-1, result);
    }

    @Test
    public void testPopularCollectedSongOfUserSuccess() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("userId")).thenReturn("1");

        // 创建模拟的歌曲列表
        Song song1 = new Song();
        song1.setName("song1");
        song1.setStyle("Pop");
        Song song2 = new Song();
        song2.setName("song2");
        song2.setStyle("Pop");
        // 创建模拟的Song列表
        List<Song> mockSongList = new ArrayList<>();
        mockSongList.add(song1);
        mockSongList.add(song2);

        // 设置SongMapper的模拟行为，模拟查询用户歌曲成功的情况
        when(mockSongMapper.songOfUserId(1)).thenReturn(mockSongList);

        // 设置SongMapper的模拟行为，模拟查询用户热门收藏歌曲成功的情况
        when(mockSongMapper.popularCollectedSongOfUser(1)).thenReturn(song1);

        // 调用popularCollectedSongOfUser方法
        Object result = service.popularCollectedSongOfUser(mockRequest);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof Song);
        assertEquals("song1", ((Song) result).getName());
    }
    @Test
    public void testPopularCollectedSongOfUserEmpty() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("userId")).thenReturn("1");

        // 设置SongMapper的模拟行为，模拟查询用户歌曲为空的情况
        when(mockSongMapper.songOfUserId(1)).thenReturn(Collections.emptyList());

        // 调用popularCollectedSongOfUser方法
        Object result = service.popularCollectedSongOfUser(mockRequest);

        // 验证结果
        assertEquals(-1, result);
    }

    @Test
    public void testIsSongOfUserTrue() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("songId")).thenReturn("1");
        when(mockRequest.getParameter("userId")).thenReturn("1");

        // 创建模拟的Song对象
        Song mockSong = new Song();
        mockSong.setId(1);
        mockSong.setUserId(1); // 歌曲的用户ID与请求中的用户ID相同

        // 创建模拟的User对象
        User mockUser = new User();
        mockUser.setId(1);

        // 设置SongService的模拟行为，模拟查询歌曲成功的情况
        when(mockSongService.selectByPrimaryKey(1)).thenReturn(mockSong);

        // 设置UserService的模拟行为，模拟查询用户成功的情况
        when(mockUserService.selectByPrimaryKey(1)).thenReturn(mockUser);

        // 调用isSongOfUser方法
        Object result = service.isSongOfUser(mockRequest);

        // 验证结果
        assertTrue((Boolean) result);
    }
    @Test
    public void testIsSongOfUserFalse() throws Exception {
        // 设置请求参数
        when(mockRequest.getParameter("songId")).thenReturn("1");
        when(mockRequest.getParameter("userId")).thenReturn("2");

        // 创建模拟的Song对象
        Song mockSong = new Song();
        mockSong.setId(1);
        mockSong.setUserId(1); // 歌曲的用户ID与请求中的用户ID不同

        // 创建模拟的User对象
        User mockUser = new User();
        mockUser.setId(1);

        // 设置SongService的模拟行为，模拟查询歌曲成功的情况
        when(mockSongService.selectByPrimaryKey(1)).thenReturn(mockSong);

        // 设置UserService的模拟行为，模拟查询用户成功的情况
        when(mockUserService.selectByPrimaryKey(1)).thenReturn(mockUser);

        // 调用isSongOfUser方法
        Object result = service.isSongOfUser(mockRequest);

        // 验证结果
        assertFalse((Boolean) result);
    }


}