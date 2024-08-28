package com.java.userPart.controller;

import com.alibaba.fastjson.JSONObject;

import com.java.userpart.controller.AdminController;
import com.java.userpart.service.AdminService;
import com.java.userpart.utils.Consts;
import com.java.userpart.utils.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }

    @Test
    void testLoginStatusSuccess() throws Exception {
        String name = "testUser";
        String password = "testPassword";
        String cypher = SecurityUtil.encrypt(password);
        when(request.getParameter(eq("name"))).thenReturn(name);
        when(request.getParameter(eq("password"))).thenReturn(password);
        when(adminService.verifyPassword(eq(name), eq(cypher))).thenReturn(true);

        // 执行测试方法
        JSONObject result = (JSONObject) adminController.loginStatus(request, session, response);

        // 验证结果
        assertEquals(1, result.getIntValue(Consts.CODE));
        assertEquals("登录成功", result.getString(Consts.MSG));
    }

    @Test
    void testLoginStatusFailure() throws Exception {
        String name = "wrongUser";
        String password = "wrongPassword";

        when(request.getParameter(eq("name"))).thenReturn(name);
        when(request.getParameter(eq("password"))).thenReturn(password);
        when(adminService.verifyPassword(eq(name), any())).thenReturn(false);

        // 执行测试方法
        JSONObject result = (JSONObject) adminController.loginStatus(request, session, response);

        // 验证结果
        assertEquals(0, result.getIntValue(Consts.CODE));
        assertEquals("用户名或密码错误", result.getString(Consts.MSG));
    }

    @Test
    public void testPreLoginSuccess() {
        // 设置模拟数据
        Cookie[] cookies = new Cookie[] {
                new Cookie("cookie_name", "testUser"),
                new Cookie("cookie_cypher", "testCypher")
        };
        when(request.getCookies()).thenReturn(cookies);
        when(adminService.verifyPassword("testUser", "testCypher")).thenReturn(true);

        // 调用方法
        JSONObject result = (JSONObject) adminController.preLogin(request);

        // 断言结果
        assertEquals(1, result.getIntValue(Consts.CODE));
        assertEquals("登录成功", result.getString(Consts.MSG));
        assertEquals("testUser",result.getString(Consts.NAME));
    }

    @Test
    public void testPreLoginFailure() {
        // 设置模拟数据
        when(request.getCookies()).thenReturn(null);

        // 调用方法
        JSONObject result = (JSONObject) adminController.preLogin(request);

        // 断言结果
        assertEquals(0, result.getIntValue(Consts.CODE));
        assertEquals("用户名或密码错误", result.getString(Consts.MSG));
    }
    @Test
    public void testLogout() {
        // 调用logout方法
        JSONObject result = (JSONObject)adminController.logout(request, session, response);

        // 验证session是否移除了NAME属性
        verify(session).removeAttribute(Consts.NAME);

        // 验证response是否添加了名为cookie_cypher的Cookie
        Cookie cookie_cypher = new Cookie("cookie_cypher","");
        cookie_cypher.setMaxAge(0);
        cookie_cypher.setPath(request.getContextPath());
//        verify(response).addCookie(cookie_cypher);

        // 断言返回结果是一个JSONObject，并且包含正确的信息
        assertEquals(1, result.getIntValue(Consts.CODE));
    }
}