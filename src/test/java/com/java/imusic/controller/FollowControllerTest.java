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

class FollowControllerTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FollowService followService;

    @InjectMocks
    private FollowController followController;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }
    @Test
    public void testAddFollow_AlreadyFollowed() throws Exception {
        // 模拟请求参数
        Integer validUserId = 1;
        Integer validSingerId = 2;
        when(request.getParameter("userId")).thenReturn(validUserId.toString());
        when(request.getParameter("singerId")).thenReturn(validSingerId.toString());

        // 模拟followService.existFollow方法的返回值
        when(followService.existFollow(validUserId, validSingerId)).thenReturn(true);

        // 调用addFollow方法
        Object result = followController.addFollow(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(0, jsonObject.getIntValue(Consts.CODE));
        assertEquals("已关注", jsonObject.getString(Consts.MSG));

        // 验证followService.existFollow是否被调用
        verify(followService).existFollow(validUserId, validSingerId);
    }

    @Test
    public void testAddFollow_Success() throws Exception {
        // 模拟请求参数
        Integer validUserId = 1;
        Integer validSingerId = 2;
        when(request.getParameter("userId")).thenReturn(validUserId.toString());
        when(request.getParameter("singerId")).thenReturn(validSingerId.toString());

        // 模拟followService.existFollow方法的返回值
        when(followService.existFollow(validUserId, validSingerId)).thenReturn(false);

        // 模拟followService.insert方法的返回值
        when(followService.insert(any(Follow.class))).thenReturn(1);

        // 调用addFollow方法
        Object result = followController.addFollow(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(1, jsonObject.getIntValue(Consts.CODE));
        assertEquals("关注成功", jsonObject.getString(Consts.MSG));

        // 验证followService.insert是否被调用
        verify(followService).insert(any(Follow.class));
    }

    @Test
    public void testAddFollow_Failure() throws Exception {
        // 模拟请求参数
        Integer validUserId = 1;
        Integer validSingerId = 2;
        when(request.getParameter("userId")).thenReturn(validUserId.toString());
        when(request.getParameter("singerId")).thenReturn(validSingerId.toString());

        // 模拟followService.existFollow方法的返回值
        when(followService.existFollow(validUserId, validSingerId)).thenReturn(false);

        // 模拟followService.insert方法的返回值
        when(followService.insert(any(Follow.class))).thenReturn(0);

        // 调用addFollow方法
        Object result = followController.addFollow(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(0, jsonObject.getIntValue(Consts.CODE));
        assertEquals("关注失败", jsonObject.getString(Consts.MSG));

        // 验证followService.insert是否被调用
        verify(followService).insert(any(Follow.class));
    }

    @Test
    public void testDeleteFollow_Success() throws Exception {
        // 模拟请求参数
        Integer validId = 1; // 假设有效的id为1
        when(request.getParameter("id")).thenReturn(validId.toString());

        // 模拟followService.delete方法的返回值
        when(followService.delete(validId)).thenReturn(1);

        // 调用deleteFollow方法
        Object result = followController.deleteFollow(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(1, jsonObject.getIntValue(Consts.CODE));
        assertEquals("取消成功", jsonObject.getString(Consts.MSG));

        // 验证followService.delete是否被调用
        verify(followService).delete(validId);
    }

    @Test
    public void testDeleteFollow_Failure() throws Exception {
        // 模拟请求参数
        Integer validId = 1;
        when(request.getParameter("id")).thenReturn(validId.toString());

        // 模拟followService.delete方法的返回值
        when(followService.delete(validId)).thenReturn(0);

        // 调用deleteFollow方法
        Object result = followController.deleteFollow(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(0, jsonObject.getIntValue(Consts.CODE));
        assertEquals("取消失败", jsonObject.getString(Consts.MSG));
    }

    @Test
    public void testDeleteByUserIdAndSingerId_Success() throws Exception {
        // 模拟请求参数
        Integer validUserId = 1;
        Integer validSingerId = 2;
        when(request.getParameter("userId")).thenReturn(validUserId.toString());
        when(request.getParameter("singerId")).thenReturn(validSingerId.toString());

        // 模拟followService.selectByUserIdAndSingerId方法的返回值
        Follow follow = new Follow();
        follow.setId(1); // 假设返回一个有效的Follow对象
        when(followService.selectByUserIdAndSingerId(validUserId, validSingerId)).thenReturn(follow);

        // 模拟followService.delete方法的返回值
        when(followService.delete(follow.getId())).thenReturn(1);

        // 调用deleteByUserIdAndSingerId方法
        Object result = followController.deleteByUserIdAndSingerId(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(1, jsonObject.getIntValue(Consts.CODE));
        assertEquals("取消成功", jsonObject.getString(Consts.MSG));

        // 验证followService.selectByUserIdAndSingerId和delete是否被调用
        verify(followService).selectByUserIdAndSingerId(validUserId, validSingerId);
        verify(followService).delete(follow.getId());
    }

    @Test
    public void testDeleteByUserIdAndSingerId_NotFollowed() throws Exception {
        // 模拟请求参数
        Integer validUserId = 1;
        Integer validSingerId = 2;
        when(request.getParameter("userId")).thenReturn(validUserId.toString());
        when(request.getParameter("singerId")).thenReturn(validSingerId.toString());

        // 模拟followService.selectByUserIdAndSingerId方法的返回值，未关注的情况
        when(followService.selectByUserIdAndSingerId(validUserId, validSingerId)).thenReturn(null);

        // 调用deleteByUserIdAndSingerId方法
        Object result = followController.deleteByUserIdAndSingerId(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(0, jsonObject.getIntValue(Consts.CODE));
        assertEquals("未关注", jsonObject.getString(Consts.MSG));

        // 验证followService.selectByUserIdAndSingerId是否被调用
        verify(followService).selectByUserIdAndSingerId(validUserId, validSingerId);
        // 验证followService.delete没有被调用
        verify(followService, never()).delete(anyInt());
    }

    @Test
    public void testDeleteByUserIdAndSingerId_DeletionFailure() throws Exception {
        // 模拟请求参数
        Integer validUserId = 1;
        Integer validSingerId = 2;
        when(request.getParameter("userId")).thenReturn(validUserId.toString());
        when(request.getParameter("singerId")).thenReturn(validSingerId.toString());

        // 模拟followService.selectByUserIdAndSingerId方法的返回值
        Follow follow = new Follow();
        follow.setId(1); // 假设返回一个有效的Follow对象
        when(followService.selectByUserIdAndSingerId(validUserId, validSingerId)).thenReturn(follow);

        // 模拟followService.delete方法的返回值，删除失败的情况
        when(followService.delete(follow.getId())).thenReturn(0);

        // 调用deleteByUserIdAndSingerId方法
        Object result = followController.deleteByUserIdAndSingerId(request);

        // 断言返回结果为JSONObject
        assertTrue(result instanceof JSONObject);
        JSONObject jsonObject = (JSONObject) result;

        // 断言返回的JSON对象包含正确的状态码和消息
        assertEquals(0, jsonObject.getIntValue(Consts.CODE));
        assertEquals("取消失败", jsonObject.getString(Consts.MSG));

        // 验证followService.delete是否被调用
        verify(followService).delete(follow.getId());
    }


    @Test
    public void testSelectByPrimaryKey_Success() throws Exception {
        // 模拟请求参数
        String validId = "1"; // 假设有效的主键id为1
        when(request.getParameter("id")).thenReturn(validId.trim());

        // 模拟followService.selectByPrimaryKey方法的返回值
        Follow expectedFollow = new Follow(/* 填充数据 */);
        when(followService.selectByPrimaryKey(Integer.parseInt(validId))).thenReturn(expectedFollow);

        // 调用selectByPrimaryKey方法
        Object result = followController.selectByPrimaryKey(request);

        // 断言返回结果为Follow对象
        assertTrue(result instanceof Follow);
        Follow resultFollow = (Follow) result;

        // 断言返回的Follow对象与预期的Follow对象相匹配
        assertEquals(expectedFollow, resultFollow);

        // 验证followService.selectByPrimaryKey是否被调用
        verify(followService).selectByPrimaryKey(Integer.parseInt(validId));
    }

    @Test
    public void testSelectByPrimaryKey_InvalidId() throws Exception {
        // 模拟请求参数，id为空字符串
        when(request.getParameter("id")).thenReturn("-1");

        // 调用selectByPrimaryKey方法
        Object result = followController.selectByPrimaryKey(request);

        // 断言返回结果为null，或者根据业务逻辑可能抛出异常
        assertNull(result);
    }

    @Test
    public void testSelectByUserIdAndSingerId_Success() throws Exception {
        // 模拟请求参数
        Integer validUserId = 1;
        Integer validSingerId = 2;
        when(request.getParameter("userId")).thenReturn(String.valueOf(validUserId));
        when(request.getParameter("singerId")).thenReturn(String.valueOf(validSingerId));

        // 模拟followService.selectByUserIdAndSingerId方法的返回值
        Follow expectedFollow = new Follow(/* 填充数据 */);
        when(followService.selectByUserIdAndSingerId(validUserId, validSingerId)).thenReturn(expectedFollow);

        // 调用selectByUserIdAndSingerId方法
        Object result = followController.selectByUserIdAndSingerId(request);

        // 断言返回结果为Follow对象
        assertTrue(result instanceof Follow);
        Follow resultFollow = (Follow) result;

        // 断言返回的Follow对象与预期的Follow对象相匹配
        assertEquals(expectedFollow, resultFollow);

        // 验证followService.selectByUserIdAndSingerId是否被调用
        verify(followService).selectByUserIdAndSingerId(validUserId, validSingerId);
    }

    @Test
    public void testSelectByUserIdAndSingerId_NotFound() throws Exception {
        // 模拟请求参数
        Integer validUserId = 1;
        Integer validSingerId = 2;
        when(request.getParameter("userId")).thenReturn(String.valueOf(validUserId));
        when(request.getParameter("singerId")).thenReturn(String.valueOf(validSingerId));

        // 模拟followService.selectByUserIdAndSingerId方法的返回值，未找到的情况
        when(followService.selectByUserIdAndSingerId(validUserId, validSingerId)).thenReturn(null);

        // 调用selectByUserIdAndSingerId方法
        Object result = followController.selectByUserIdAndSingerId(request);

        // 断言返回结果为null
        assertNull(result);

        // 验证followService.selectByUserIdAndSingerId是否被调用
        verify(followService).selectByUserIdAndSingerId(validUserId, validSingerId);
    }
    @Test
    public void testExistFollow_Exists() throws Exception {
        // 模拟请求参数
        Integer validUserId = 1;
        Integer validSingerId = 2;
        when(request.getParameter("userId")).thenReturn(String.valueOf(validUserId));
        when(request.getParameter("singerId")).thenReturn(String.valueOf(validSingerId));

        // 模拟followService.existFollow方法的返回值，表示存在关注关系
        when(followService.existFollow(validUserId, validSingerId)).thenReturn(true);

        // 调用existFollow方法
        Object result = followController.existFollow(request);

        // 断言返回结果为boolean类型，并且为true
        assertTrue((Boolean) result);

        // 验证followService.existFollow是否被调用
        verify(followService).existFollow(validUserId, validSingerId);
    }

    @Test
    public void testExistFollow_NotExists() throws Exception {
        // 模拟请求参数
        Integer validUserId = 1;
        Integer validSingerId = 2;
        when(request.getParameter("userId")).thenReturn(String.valueOf(validUserId));
        when(request.getParameter("singerId")).thenReturn(String.valueOf(validSingerId));

        // 模拟followService.existFollow方法的返回值，表示不存在关注关系
        when(followService.existFollow(validUserId, validSingerId)).thenReturn(false);

        // 调用existFollow方法
        Object result = followController.existFollow(request);

        // 断言返回结果为boolean类型，并且为false
        assertFalse((Boolean) result);

        // 验证followService.existFollow是否被调用
        verify(followService).existFollow(validUserId, validSingerId);
    }

    @Test
    public void testGetByUserId_Success() throws Exception {
        // 模拟请求参数
        Integer validUserId = 1; // 假设有效的用户ID为1
        when(request.getParameter("userId")).thenReturn(String.valueOf(validUserId));

        // 模拟followService.getByUserId方法的返回值
        List<Follow> expectedFollowList = Arrays.asList(new Follow(/* 填充数据 */), new Follow(/* 填充数据 */));
        when(followService.getByUserId(validUserId)).thenReturn(expectedFollowList);

        // 调用getByUserId方法
        Object result = followController.getByUserId(request);

        // 断言返回结果为List对象
        assertTrue(result instanceof List);
        List<Follow> resultFollowList = (List<Follow>) result;

        // 断言返回的列表与预期的列表相匹配
        assertEquals(expectedFollowList, resultFollowList);

        // 验证followService.getByUserId是否被调用
        verify(followService).getByUserId(validUserId);
    }

    @Test
    public void testGetByUserId_InvalidUserId() throws Exception {
        // 模拟请求参数，userId为空字符串
        when(request.getParameter("userId")).thenReturn("-1");

        // 调用getByUserId方法
        Object result = followController.getByUserId(request);

        // 断言返回结果为null或空列表，取决于你的业务逻辑如何处理这种情况
        assertTrue(result instanceof List);
        List<Follow> resultFollowList = (List<Follow>) result;
        assertTrue(resultFollowList.isEmpty());

        // 验证followService.getByUserId是否被调用
        // 如果业务逻辑中会捕获异常并返回空列表，则此处应有verify
        // verify(followService, never()).getByUserId(anyInt());
    }

    @Test
    public void testGetCountByUserId_Success() throws Exception {
        // 模拟请求参数
        Integer validUserId = 1; // 假设有效的用户ID为1
        when(request.getParameter("userId")).thenReturn(String.valueOf(validUserId));

        // 模拟followService.getCountByUserId方法的返回值
        int expectedCount = 10; // 假设预期的关注数量为10
        when(followService.getCountByUserId(validUserId)).thenReturn(expectedCount);

        // 调用getCountByUserId方法
        Object result = followController.getCountByUserId(request);

        // 断言返回结果为Integer对象
        assertTrue(result instanceof Integer);
        int resultCount = (Integer) result;

        // 断言返回的数量与预期的数量相匹配
        assertEquals(expectedCount, resultCount);

        // 验证followService.getCountByUserId是否被调用
        verify(followService).getCountByUserId(validUserId);
    }

    @Test
    public void testGetCountByUserId_InvalidUserId() throws Exception {
        // 模拟请求参数，userId为空字符串
        when(request.getParameter("userId")).thenReturn("-1");

        // 调用getCountByUserId方法
        Object result = followController.getCountByUserId(request);

        // 断言返回结果为0或null，取决于你的业务逻辑如何处理这种情况
        assertEquals(0, result); // 或者使用assertNull(result);

        // 验证followService.getCountByUserId是否被调用
        // 如果业务逻辑中会捕获异常并返回0，则此处应有verify
        // verify(followService, never()).getCountByUserId(anyInt());
    }

}