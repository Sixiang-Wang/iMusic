package com.java.imusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.imusic.domain.Comment;
import com.java.imusic.service.CommentService;
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

class CommentControllerTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletResponse response;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }

    @Test
    public void testAddComment_Success() {
        // 模拟请求参数
        when(request.getParameter("userId")).thenReturn("1");
        when(request.getParameter("type")).thenReturn("0");
        when(request.getParameter("songId")).thenReturn("1001");
        when(request.getParameter("content")).thenReturn("Great song!");

        // 模拟commentService.insert方法的返回值
        when(commentService.insert(any(Comment.class))).thenReturn(true);

        // 调用addComment方法
        Object result = commentController.addComment(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(1, jsonObject.getIntValue(Consts.CODE));
        assertEquals("评论成功", jsonObject.getString(Consts.MSG));

        // 验证commentService.insert是否被调用
        verify(commentService).insert(any(Comment.class));
    }

    @Test
    public void testAddComment_Failure() {
        // 模拟请求参数
        when(request.getParameter("userId")).thenReturn("1");
        when(request.getParameter("type")).thenReturn("0");
        when(request.getParameter("songId")).thenReturn("1001");
        when(request.getParameter("content")).thenReturn(" ");

        // 模拟commentService.insert方法的返回值
        when(commentService.insert(any(Comment.class))).thenReturn(false);

        // 调用addComment方法
        Object result = commentController.addComment(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(0, jsonObject.getIntValue(Consts.CODE));
        assertEquals("评论失败", jsonObject.getString(Consts.MSG));
    }

    @Test
    public void testDeleteComment_Success() {
        // 模拟请求参数
        String validId = "1"; // 假设有效的主键id为1
        when(request.getParameter("id")).thenReturn(validId);

        // 模拟commentService.delete方法的返回值
        when(commentService.delete(anyInt())).thenReturn(true);

        // 调用deleteComment方法
        Object result = commentController.deleteComment(request);

        // 断言返回结果为true
        assertTrue((Boolean) result);

        // 验证commentService.delete是否被调用
        verify(commentService).delete(Integer.parseInt(validId));
    }

    @Test
    public void testDeleteComment_Failure() {
        // 模拟请求参数
        String validId = "1";
        when(request.getParameter("id")).thenReturn(validId);

        // 模拟commentService.delete方法的返回值
        when(commentService.delete(anyInt())).thenReturn(false);

        // 调用deleteComment方法
        Object result = commentController.deleteComment(request);

        // 断言返回结果为false
        assertFalse((Boolean) result);
    }

    @Test
    public void testSelectByPrimaryKey_Success() {
        // 模拟请求参数
        String validId = "1"; // 假设有效的主键id为1
        when(request.getParameter("id")).thenReturn(validId);
        Comment mockComment = new Comment(); // 初始化你的Comment对象
        // 假设Comment对象有一些属性，这里进行一些模拟赋值
        mockComment.setId(1);
        mockComment.setContent("Test comment");
        // 模拟commentService.selectByPrimaryKey方法的返回值
        when(commentService.selectByPrimaryKey(Integer.parseInt(validId))).thenReturn(mockComment);

        // 调用selectByPrimaryKey方法
        Object result = commentController.selectByPrimaryKey(request);

        // 断言返回结果为Comment对象
        assertTrue(result instanceof Comment);
        Comment resultComment = (Comment) result;

        // 断言返回的Comment对象与模拟的Comment对象相匹配
        assertEquals(mockComment.getId(), resultComment.getId());
        assertEquals(mockComment.getContent(), resultComment.getContent());

        // 验证commentService.selectByPrimaryKey是否被调用
        verify(commentService).selectByPrimaryKey(Integer.parseInt(validId));
    }

    @Test
    public void testSelectByPrimaryKey_failure() {
        // 模拟请求参数，id为空或格式不正确
        when(request.getParameter("id")).thenReturn("-1");

        // 调用selectByPrimaryKey方法
        Object result = commentController.selectByPrimaryKey(request);

        // 断言返回结果为null，假设服务层在id为空时返回null
        assertNull(result);
    }

    @Test
    public void testAllComment() {
        List<Comment> mockCommentList = new ArrayList<>();
        // 假设创建一些评论并添加到列表中
        mockCommentList.add(new Comment());
        mockCommentList.add(new Comment());

        when(commentService.allComment()).thenReturn(mockCommentList);
        // 调用allComment方法
        Object result = commentController.allComment(request);

        // 断言返回结果为List对象
        assertTrue(result instanceof List);
        List<Comment> resultCommentList = (List<Comment>) result;
        // 断言返回的列表与模拟的列表相匹配
        assertEquals(mockCommentList.size(), resultCommentList.size());
        for (int i = 0; i < mockCommentList.size(); i++) {
            assertEquals(mockCommentList.get(i).getId(), resultCommentList.get(i).getId());
            assertEquals(mockCommentList.get(i).getContent(), resultCommentList.get(i).getContent());
        }

        // 验证commentService.allComment是否被调用
        verify(commentService).allComment();
    }

    @Test
    public void testCommentOfSongId_ValidId() {
        List<Comment> mockCommentList = new ArrayList<>();
        // 假设创建一些评论并添加到列表中
        mockCommentList.add(new Comment());
        mockCommentList.add(new Comment());
        // 模拟commentService.commentOfSongId方法的返回值
        when(commentService.commentOfSongId(anyInt())).thenReturn(mockCommentList);
        // 模拟请求参数
        String validSongId = "1"; // 假设有效的songId为1
        when(request.getParameter("songId")).thenReturn(validSongId);

        // 调用commentOfSongId方法
        Object result = commentController.commentOfSongId(request);

        // 断言返回结果为List对象
        assertTrue(result instanceof List);
        List<Comment> resultCommentList = (List<Comment>) result;

        // 断言返回的列表与模拟的列表相匹配
        assertEquals(mockCommentList, resultCommentList);

        // 验证commentService.commentOfSongId是否被调用
        verify(commentService).commentOfSongId(Integer.parseInt(validSongId));
    }

    @Test
    public void testCommentOfSongId_InvalidId() {
        // 模拟请求参数，songId为空或格式不正确
        when(request.getParameter("songId")).thenReturn("-1");

        // 调用commentOfSongId方法
        Object result = commentController.commentOfSongId(request);

        // 断言返回结果为null或空列表，取决于你的业务逻辑
        assertTrue(((List<Comment>) result).isEmpty());
    }

    @Test
    public void testCommentOfSongListId_ValidId() {
        List<Comment> mockCommentList = new ArrayList<>();
        // 假设创建一些评论并添加到列表中
        mockCommentList.add(new Comment());
        mockCommentList.add(new Comment());
        // 模拟commentService.commentOfSongId方法的返回值
        when(commentService.commentOfSongListId(anyInt())).thenReturn(mockCommentList);
        // 模拟请求参数
        String validSongListId = "1"; // 假设有效的songId为1
        when(request.getParameter("songListId")).thenReturn(validSongListId);

        // 调用commentOfSongId方法
        Object result = commentController.commentOfSongListId(request);

        // 断言返回结果为List对象
        assertTrue(result instanceof List);
        List<Comment> resultCommentList = (List<Comment>) result;

        // 断言返回的列表与模拟的列表相匹配
        assertEquals(mockCommentList, resultCommentList);

        // 验证commentService.commentOfSongId是否被调用
        verify(commentService).commentOfSongListId(Integer.parseInt(validSongListId));
    }

    @Test
    public void testCommentOfSongListId_InvalidId() {
        // 模拟请求参数，songId为空或格式不正确
        when(request.getParameter("songListId")).thenReturn("-1");

        // 调用commentOfSongId方法
        Object result = commentController.commentOfSongListId(request);

        // 断言返回结果为null或空列表，取决于你的业务逻辑
        assertTrue(((List<Comment>) result).isEmpty());
    }

    @Test
    public void testLike_Success() {
        // 模拟请求参数
        when(request.getParameter("id")).thenReturn("123");
        when(request.getParameter("up")).thenReturn("456");

        // 模拟commentService.update方法的返回值
        when(commentService.update(any(Comment.class))).thenReturn(true);

        // 调用like方法
        Object result = commentController.like(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(1, jsonObject.getIntValue(Consts.CODE));
        assertEquals("点赞成功", jsonObject.getString(Consts.MSG));

        // 验证commentService.update是否被调用
        verify(commentService).update(any(Comment.class));
    }

    @Test
    public void testLike_Failure() {
        // 模拟请求参数
        when(request.getParameter("id")).thenReturn("123");
        when(request.getParameter("up")).thenReturn("456");

        // 模拟commentService.update方法的返回值
        when(commentService.update(any(Comment.class))).thenReturn(false);

        // 调用like方法
        Object result = commentController.like(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(0, jsonObject.getIntValue(Consts.CODE));
        assertEquals("点赞失败", jsonObject.getString(Consts.MSG));
    }
}