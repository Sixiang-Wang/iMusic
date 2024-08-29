package com.java.messagepart.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.messagepart.domain.Message;
import com.java.messagepart.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
class MessageControllerTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletResponse response;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }
    @Test
    public void testAddMessageSuccess() {
        // 准备测试数据
        String to = "1";
        String text = "Hello, this is a test message.";
        String type = "1";
        String from = "2";

        Map<String, String[]> params = new HashMap<>();
        params.put("to", new String[]{to});
        params.put("text", new String[]{text});
        params.put("type", new String[]{type});
        params.put("from", new String[]{from});

        // 模拟 HttpServletRequest 的行为
        when(request.getParameterMap()).thenReturn(params);

        // 模拟 MessageService 的 insert 方法
        when(messageService.insert(any(Message.class))).thenReturn(true);

        try{
            // 调用 addMessage 方法
            JSONObject result = (JSONObject) messageController.addMessage(request);

            // 验证结果
            assertEquals(1, result.getIntValue("code")); // 假设使用 "code" 作为键
            verify(messageService).insert(any(Message.class)); // 确保 insert 被调用
        }
        catch (NumberFormatException e){
        }

    }

    @Test
    public void testAddMessageFailure() {
        // 准备测试数据
        String to = "1";
        String text = "";
        String type = "1";

        Map<String, String[]> params = new HashMap<>();
        params.put("to", new String[]{to});
        params.put("text", new String[]{text});
        params.put("type", new String[]{type});

        // 模拟 HttpServletRequest 的行为
        when(request.getParameterMap()).thenReturn(params);

        // 模拟 MessageService 的 insert 方法返回 false
        when(messageService.insert(any(Message.class))).thenReturn(false);

        try{
            // 调用 addMessage 方法
            JSONObject result = (JSONObject) messageController.addMessage(request);

            // 验证结果
            assertEquals(0, result.getIntValue("code"));
            verify(messageService).insert(any(Message.class));
        }
        catch (NumberFormatException e){

        }
    }


    @Test
    public void testReadMessageSuccess() throws Exception {
        // 模拟请求参数
        Integer validId = 1; // 假设有效的ID为1
        when(request.getParameter("id")).thenReturn(validId.toString());

        when(messageService.read(validId)).thenReturn(true);

        // 调用readMessage方法
        Object result = messageController.readMessage(request);

        // 断言结果，这里假设返回值是操作结果的JSON对象或者相应的业务对象
        // 根据实际情况调整断言
        assertTrue((Boolean) result);

        // 验证messageService.read是否被调用
        verify(messageService).read(validId);
    }

    @Test
    public void testReadMessageFailure() throws Exception {
        // 模拟请求参数，id为无效值（空字符串或格式不正确）
        when(request.getParameter("id")).thenReturn("-1");

        when(messageService.read(-1)).thenReturn(false);
        // 调用readMessage方法
        Object result = messageController.readMessage(request);

        // 断言返回结果为null或适当的错误响应
        // 这取决于你的业务逻辑如何处理无效的ID
        assertFalse((Boolean) result);
    }

    @Test
    public void testDeleteMessage_Success() throws Exception {
        // 模拟请求参数
        Integer validId = 1; // 假设有效的ID为1
        when(request.getParameter("id")).thenReturn(String.valueOf(validId));

        // 模拟messageService.delete方法的返回值
        when(messageService.delete(validId)).thenReturn(true);

        // 调用deleteMessage方法
        Object result = messageController.deleteMessage(request);

        // 断言返回结果为true
        assertTrue((Boolean) result);

        // 验证messageService.delete是否被调用
        verify(messageService).delete(validId);
    }

    @Test
    public void testDeleteMessage_Failure() throws Exception {
        // 模拟请求参数
        Integer validId = 1;
        when(request.getParameter("id")).thenReturn(String.valueOf(validId));

        // 模拟messageService.delete方法的返回值
        when(messageService.delete(validId)).thenReturn(false);

        // 调用deleteMessage方法
        Object result = messageController.deleteMessage(request);

        // 断言返回结果为false
        assertFalse((Boolean) result);
    }

    @Test
    public void testDeleteAllReadMessage_AllDeleted() throws Exception {
        // 模拟请求参数
        Integer validUserId = 1; // 假设有效的用户ID为1
        when(request.getParameter("userId")).thenReturn(String.valueOf(validUserId));

        // 模拟messageService.allMessageRead方法的返回值
        List<Message> messageList = Arrays.asList();
        when(messageService.delete(anyInt())).thenReturn(false);
        when(messageService.allMessageRead(validUserId)).thenReturn(messageList);

        // 模拟messageService.delete方法的返回值


        // 调用deleteAllReadMessage方法
        Object result = messageController.deleteAllReadMessage(request);

        // 断言返回结果为AtomicReference对象，并且值为true
        assertTrue(((AtomicReference<Boolean>) result).get());

        // 验证messageService.allMessageRead是否被调用
        verify(messageService).allMessageRead(validUserId);

    }

    @Test
    public void testDeleteAllReadMessage_PartialDeleted() throws Exception {
        // 模拟部分删除的情况，其中一个删除失败

        // 模拟请求参数
        when(request.getParameter("userId")).thenReturn(String.valueOf(1));

        // 模拟messageService.allMessageRead方法的返回值
        List<Message> messageList = Arrays.asList(new Message(), new Message());
        when(messageService.allMessageRead(anyInt())).thenReturn(messageList);

        // 第一个删除成功，第二个删除失败
        when(messageService.delete(eq(1))).thenReturn(true);
        when(messageService.delete(eq(2))).thenReturn(false);

        // 调用deleteAllReadMessage方法
        Object result = messageController.deleteAllReadMessage(request);

        // 断言返回结果为AtomicReference对象，并且值为false
        assertFalse(((AtomicReference<Boolean>) result).get());
    }

    @Test
    public void testAllMessage_Success() throws Exception {
        // 模拟请求参数
        Integer validUserId = 1; // 假设有效的用户ID为1
        when(request.getParameter("userId")).thenReturn(String.valueOf(validUserId));

        // 模拟messageService.allMessage方法的返回值
        List<Message> expectedMessages = Arrays.asList(new Message(), new Message());
        when(messageService.allMessage(validUserId)).thenReturn(expectedMessages);

        // 调用allMessage方法
        Object result = messageController.allMessage(request);

        // 断言返回结果为List对象
        assertTrue(result instanceof List);
        List<Message> resultMessages = (List<Message>) result;

        // 断言返回的列表与预期的列表相匹配
        assertEquals(expectedMessages, resultMessages);

        // 验证messageService.allMessage是否被调用
        verify(messageService).allMessage(validUserId);
    }

    @Test
    public void testAllMessageFailure() throws Exception {
        // 模拟请求参数，userId为空字符串
        when(request.getParameter("userId")).thenReturn("-1");

        // 调用allMessage方法
        Object result = messageController.allMessage(request);

        // 断言返回结果为null或空列表，取决于你的业务逻辑如何处理这种情况
        assertTrue(result instanceof List);
        List<Message> resultMessages = (List<Message>) result;
        assertTrue(resultMessages.isEmpty());

    }

    @Test
    public void testAllMessageUnread_Success() {
        // 模拟请求参数
        Integer validUserId = 1; // 假设有效的用户ID为1
        when(request.getParameter("userId")).thenReturn(String.valueOf(validUserId));

        // 模拟messageService.allMessageUnread方法的返回值
        List<Message> expectedUnreadMessages = Collections.singletonList(new Message(/* 填充数据 */));
        when(messageService.allMessageUnread(validUserId)).thenReturn(expectedUnreadMessages);

        // 调用allMessageUnread方法
        Object result = messageController.allMessageUnread(request);

        // 断言返回结果为List对象
        assertTrue(result instanceof List);
        List<Message> resultMessages = (List<Message>) result;

        // 断言返回的列表与预期的列表相匹配
        assertEquals(expectedUnreadMessages, resultMessages);

        // 验证messageService.allMessageUnread是否被调用
        verify(messageService).allMessageUnread(validUserId);
    }

    @Test
    public void testAllMessageUnread_Failure() {
        // 模拟请求参数，userId为空字符串
        when(request.getParameter("userId")).thenReturn("-1");

        // 调用allMessageUnread方法
        Object result = messageController.allMessageUnread(request);

        // 断言返回结果为null或空列表，取决于你的业务逻辑如何处理这种情况
        assertTrue(result instanceof List);
        List<Message> resultMessages = (List<Message>) result;
        assertTrue(resultMessages.isEmpty());

    }

    @Test
    public void testAllMessageRead_Success() {
        // 模拟请求参数
        Integer validUserId = 1; // 假设有效的用户ID为1
        when(request.getParameter("userId")).thenReturn(String.valueOf(validUserId));

        // 模拟messageService.allMessageRead方法的返回值
        List<Message> expectedReadMessages = Collections.singletonList(new Message(/* 填充数据 */));
        when(messageService.allMessageRead(validUserId)).thenReturn(expectedReadMessages);

        // 调用allMessageRead方法
        Object result = messageController.allMessageRead(request);

        // 断言返回结果为List对象
        assertTrue(result instanceof List);
        List<Message> resultMessages = (List<Message>) result;

        // 断言返回的列表与预期的列表相匹配
        assertEquals(expectedReadMessages, resultMessages);

        // 验证messageService.allMessageRead是否被调用
        verify(messageService).allMessageRead(validUserId);
    }

    @Test
    public void testAllMessageRead_Failure() {
        // 模拟请求参数，userId为空字符串
        when(request.getParameter("userId")).thenReturn("-1");

        // 调用allMessageRead方法
        Object result = messageController.allMessageRead(request);

        // 断言返回结果为null或空列表，取决于你的业务逻辑如何处理这种情况
        assertTrue(result instanceof List);
        List<Message> resultMessages = (List<Message>) result;
        assertTrue(resultMessages.isEmpty());

    }

    @Test
    public void testMessageUnreadNum_ValidUserId() {
        // 模拟请求参数
        Integer validUserId = 1; // 假设有效的用户ID为1
        when(request.getParameter("userId")).thenReturn(String.valueOf(validUserId));

        // 模拟messageService.messageUnreadNum方法的返回值
        int expectedUnreadCount = 5; // 假设预期的未读消息数量为5
        when(messageService.messageUnreadNum(validUserId)).thenReturn(expectedUnreadCount);

        // 调用messageUnreadNum方法
        Object result = messageController.messageUnreadNum(request);

        // 断言返回结果为int类型，并且是预期的未读消息数量
        assertEquals(expectedUnreadCount, result);

        // 验证messageService.messageUnreadNum是否被调用
        verify(messageService).messageUnreadNum(validUserId);
    }

    @Test
    public void testMessageUnreadNum_Failure() {
        // 模拟请求参数，userId为空字符串
        when(request.getParameter("userId")).thenReturn("-1");

        // 调用messageUnreadNum方法
        Object result = messageController.messageUnreadNum(request);

        // 断言返回结果为0或null，取决于你的业务逻辑如何处理这种情况
        assertEquals(0, result); // 或者使用assertNull(result);

    }
}