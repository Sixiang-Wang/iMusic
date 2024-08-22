package com.java.imusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.imusic.dao.CommentMapper;
import com.java.imusic.domain.CommentUp;
import com.java.imusic.service.CommentService;
import com.java.imusic.service.CommentUpService;
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

class CommentUpControllerTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletResponse response;

    @Mock
    private CommentMapper commentMapper;
    @Mock
    private CommentUpService commentUpService;

    @InjectMocks
    private CommentUpController commentUpController;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }
    @Test
    public void testAddCommentUp_Success() throws Exception {
        // 模拟请求参数
        when(request.getParameter("userId")).thenReturn("1");
        when(request.getParameter("commentId")).thenReturn("100");

        // 模拟commentUpService.insert方法的返回值
        when(commentUpService.insert(any(CommentUp.class))).thenReturn(true);

        // 调用addCommentUp方法
        Object result = commentUpController.addCommentUp(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(1, jsonObject.getIntValue(Consts.CODE));
        assertEquals("点赞成功", jsonObject.getString(Consts.MSG));

        // 验证commentUpService.insert是否被调用
        verify(commentUpService).insert(any(CommentUp.class));
    }

    @Test
    public void testAddCommentUp_Failure() throws Exception {
        // 模拟请求参数
        when(request.getParameter("userId")).thenReturn("1");
        when(request.getParameter("commentId")).thenReturn("100");

        // 模拟commentUpService.insert方法的返回值
        when(commentUpService.insert(any(CommentUp.class))).thenReturn(false);

        // 调用addCommentUp方法
        Object result = commentUpController.addCommentUp(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(0, jsonObject.getIntValue(Consts.CODE));
        assertEquals("点赞失败", jsonObject.getString(Consts.MSG));

        // 验证commentUpService.insert是否被调用
        verify(commentUpService).insert(any(CommentUp.class));

    }

    @Test
    public void testDeleteCommentUp_Success() throws Exception {
        // 模拟请求参数
        Integer validUserId = 1;
        Integer validCommentId = 100;
        when(request.getParameter("userId")).thenReturn(validUserId.toString());
        when(request.getParameter("commentId")).thenReturn(validCommentId.toString());

        // 模拟commentUpService.delete方法的返回值
        when(commentUpService.delete(validUserId, validCommentId)).thenReturn(true);

        // 调用deleteCommentUp方法
        Object result = commentUpController.deleteCommentUp(request);

        // 断言返回结果为true
        assertTrue((Boolean) result);

        // 验证commentUpService.delete是否被调用
        verify(commentUpService).delete(validUserId, validCommentId);

    }

    @Test
    public void testDeleteCommentUp_Failure() throws Exception {
        // 模拟请求参数
        Integer validUserId = 1;
        Integer validCommentId = 100;
        when(request.getParameter("userId")).thenReturn(validUserId.toString());
        when(request.getParameter("commentId")).thenReturn(validCommentId.toString());

        // 模拟commentUpService.delete方法的返回值
        when(commentUpService.delete(validUserId, validCommentId)).thenReturn(false);

        // 调用deleteCommentUp方法
        Object result = commentUpController.deleteCommentUp(request);

        // 断言返回结果为false
        assertFalse((Boolean) result);

        // 验证commentUpService.delete是否被调用
        verify(commentUpService).delete(validUserId, validCommentId);
    }

    @Test
    public void testExistCommentUp_Exists() throws Exception {
        // 模拟请求参数
        Integer validUserId = 1;
        Integer validCommentId = 100;
        when(request.getParameter("userId")).thenReturn(validUserId.toString());
        when(request.getParameter("commentId")).thenReturn(validCommentId.toString());

        // 模拟commentUpService.exist方法的返回值
        when(commentUpService.exist(validUserId, validCommentId)).thenReturn(true);

        // 调用existCommentUp方法
        Object result = commentUpController.existCommentUp(request);

        // 断言返回结果为true
        assertTrue((Boolean) result);

        // 验证commentUpService.exist是否被调用
        verify(commentUpService).exist(validUserId, validCommentId);
    }

    @Test
    public void testExistCommentUp_NotExists() throws Exception {
        // 模拟请求参数
        Integer validUserId = 1;
        Integer validCommentId = 100;
        when(request.getParameter("userId")).thenReturn(validUserId.toString());
        when(request.getParameter("commentId")).thenReturn(validCommentId.toString());

        // 模拟commentUpService.exist方法的返回值
        when(commentUpService.exist(validUserId, validCommentId)).thenReturn(false);

        // 调用existCommentUp方法
        Object result = commentUpController.existCommentUp(request);

        // 断言返回结果为false
        assertFalse((Boolean) result);

        // 验证commentUpService.exist是否被调用
        verify(commentUpService).exist(validUserId, validCommentId);
    }


    @Test
    public void testSumUp_ValidId() {
        // 模拟请求参数
        Integer validCommentId = 100;
        when(request.getParameter("commentId")).thenReturn(String.valueOf(validCommentId));

        // 模拟commentMapper.sumUp方法的返回值
        int expectedSum = 10; // 假设预期的点赞总数为10
        when(commentMapper.sumUp(validCommentId)).thenReturn(expectedSum);

        // 调用sumUp方法
        Object result = commentUpController.sumUp(request);

        // 断言返回结果为预期的点赞总数
        assertEquals(expectedSum, result);
    }
    @Test
    public void testSumUp_InvalidId() throws NumberFormatException{
        // 模拟请求参数
        Integer validCommentId = 100;
        when(request.getParameter("commentId")).thenReturn(String.valueOf(validCommentId));

        // 模拟commentMapper.sumUp方法的返回值
        int expectedSum = 10; // 假设预期的点赞总数为10
        when(commentMapper.sumUp(validCommentId)).thenReturn(expectedSum);

        // 调用sumUp方法
        Object result = commentUpController.sumUp(request);

        // 断言返回结果为预期的点赞总数
        assertNotEquals(0, result);
    }
}