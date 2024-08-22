package com.java.imusic.controller;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.java.imusic.utils.MailUtil;
import lombok.Getter;
import org.json.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailSender;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class MailControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private MailUtil mailUtil;
    @InjectMocks
    private MailController mailController; // 假设这是你的控制器类

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
        // 由于mailUtil是工具类，我们使用spy来模拟静态方法
    }
    @Test
    public void testSendMailSuccess() throws JSONException {
        // 准备测试数据
        String expectedTo = "test@example.com";
        String expectedVerifyCode = "123456";
        Map<String, String[]> params = new HashMap<>();
        params.put("to", new String[]{expectedTo});

        // 模拟 HttpServletRequest 的行为
        when(request.getParameter("to")).thenReturn(expectedTo);

        // 模拟 MailUtil 的 sendMail 方法
        when(mailUtil.sendMail(expectedTo)).thenReturn(expectedVerifyCode);

        // 调用 sendMail 方法
        JSONObject result = (JSONObject) mailController.sendMail(request);

        // 验证结果
        assertEquals(expectedVerifyCode, result.getString("verifyCode"));
    }
    @Test
    public void testSendMailFailure() throws JSONException {
        String invalidEmail = "invalidemail";
        when(request.getParameter("to")).thenReturn(invalidEmail);
        when(mailUtil.sendMail(invalidEmail)).thenReturn(null); // 假设 sendMail 返回 null 表示失败

        JSONObject result = (JSONObject) mailController.sendMail(request);

        // 验证结果，期望返回 null 或者错误信息
        assertNull(result.getString("verifyCode"));
    }
}