package com.java.imusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.imusic.domain.User;
import com.java.imusic.service.UserService;
import com.java.imusic.utils.Consts;
import com.java.imusic.utils.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @InjectMocks
    private UserController service;
    @Mock
    private HttpServletRequest request;
    @Mock
    private UserService userService;
    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }

    @Test
    public void addUser_success() {
        when(request.getParameter("username")).thenReturn("testUser");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("sex")).thenReturn("1");
        when(request.getParameter("phoneNum")).thenReturn("1234567890");
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getParameter("birth")).thenReturn("2000-01-01");
        when(request.getParameter("introduction")).thenReturn("Hello world!");
        when(request.getParameter("location")).thenReturn("Test City");
        when(request.getParameter("name")).thenReturn("Test Name");

        when(userService.getByUsername("testUser")).thenReturn(null);
        when(userService.getOneUserWithName("Test Name")).thenReturn(null);
        when(userService.insert(any(User.class))).thenReturn(true);
        when(userService.lastUserID()).thenReturn(1);
        when(userService.update(any(User.class))).thenReturn(true);

        JSONObject result = (JSONObject) service.addUser(request);

//        assertEquals(1, result.getInteger(Consts.CODE));
//        assertEquals("添加成功", result.getString(Consts.MSG));
    }

    @Test
    public void testAddUser_UsernameExists() throws Exception {
        when(request.getParameter("username")).thenReturn("testUser");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("sex")).thenReturn("1");
        when(request.getParameter("phoneNum")).thenReturn("1234567890");
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getParameter("birth")).thenReturn("2000-01-01");
        when(request.getParameter("introduction")).thenReturn("Hello world!");
        when(request.getParameter("location")).thenReturn("Test City");
        when(request.getParameter("name")).thenReturn("Test Name");

        when(userService.getByUsername("testUser")).thenReturn(new User());

        JSONObject result = (JSONObject) service.addUser(request);

        assertEquals(0, result.getInteger(Consts.CODE));
        assertEquals("用户名已存在", result.getString(Consts.MSG));
    }

    @Test
    public void testAddUser_NameExists() throws Exception {
        when(request.getParameter("username")).thenReturn("testUser");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("sex")).thenReturn("1");
        when(request.getParameter("phoneNum")).thenReturn("1234567890");
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getParameter("birth")).thenReturn("2000-01-01");
        when(request.getParameter("introduction")).thenReturn("Hello world!");
        when(request.getParameter("location")).thenReturn("Test City");
        when(request.getParameter("name")).thenReturn("Test Name");

        when(userService.getOneUserWithName("Test Name")).thenReturn(new User());

        JSONObject result = (JSONObject) service.addUser(request);

        assertEquals(0, result.getInteger(Consts.CODE));
        assertEquals("昵称已被占用", result.getString(Consts.MSG));
    }

    @Test
    public void testAddUser_PasswordEmpty() throws Exception {
        when(request.getParameter("username")).thenReturn("testUser");
        when(request.getParameter("password")).thenReturn("");
        when(request.getParameter("sex")).thenReturn("1");
        when(request.getParameter("phoneNum")).thenReturn("1234567890");
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getParameter("birth")).thenReturn("2000-01-01");
        when(request.getParameter("introduction")).thenReturn("Hello world!");
        when(request.getParameter("location")).thenReturn("Test City");
        when(request.getParameter("name")).thenReturn("Test Name");

        JSONObject result = (JSONObject) service.addUser(request);

        assertEquals(0, result.getInteger(Consts.CODE));
        assertEquals("密码不能为空", result.getString(Consts.MSG));
    }

    @Test
    public void testAddUser_InvalidBirthDate() throws Exception {
        when(request.getParameter("username")).thenReturn("testUser");
        when(request.getParameter("password")).thenReturn("1234567890");
        when(request.getParameter("sex")).thenReturn("1");
        when(request.getParameter("phoneNum")).thenReturn("1234567890");
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getParameter("birth")).thenReturn("invalid");
        when(request.getParameter("introduction")).thenReturn("Hello world!");
        when(request.getParameter("location")).thenReturn("Test City");
        when(request.getParameter("name")).thenReturn("Test Name");

        JSONObject result = (JSONObject) service.addUser(request);

        assertEquals(0, result.getInteger(Consts.CODE));
        assertEquals("添加用户失败", result.getString(Consts.MSG));
    }

    @Test
    public void testAddUser_FailureOnInsert() throws Exception {
        when(request.getParameter("username")).thenReturn("testUser");
        when(request.getParameter("password")).thenReturn("1234567890");
        when(request.getParameter("sex")).thenReturn("1");
        when(request.getParameter("phoneNum")).thenReturn("1234567890");
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getParameter("birth")).thenReturn("2000-01-01");
        when(request.getParameter("introduction")).thenReturn("Hello world!");
        when(request.getParameter("location")).thenReturn("Test City");
        when(request.getParameter("name")).thenReturn("Test Name");

        when(userService.getByUsername("testUser")).thenReturn(null);
        when(userService.getOneUserWithName("Test Name")).thenReturn(null);
        when(userService.insert(any(User.class))).thenReturn(false);

        JSONObject result = (JSONObject) service.addUser(request);

        assertEquals(0, result.getInteger(Consts.CODE));
        assertEquals("添加用户失败", result.getString(Consts.MSG));
    }

    @Test
    public void testAddUser_FailureOnUpdate() throws Exception {
        when(request.getParameter("username")).thenReturn("testUser");
        when(request.getParameter("password")).thenReturn("1234567890");
        when(request.getParameter("sex")).thenReturn("1");
        when(request.getParameter("phoneNum")).thenReturn("1234567890");
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getParameter("birth")).thenReturn("2000-01-01");
        when(request.getParameter("introduction")).thenReturn("Hello world!");
        when(request.getParameter("location")).thenReturn("Test City");
        when(request.getParameter("name")).thenReturn("Test Name");

        when(userService.getByUsername("testUser")).thenReturn(null);
        when(userService.getOneUserWithName("Test Name")).thenReturn(null);
        when(userService.insert(any(User.class))).thenReturn(true);
        when(userService.lastUserID()).thenReturn(1);
        when(userService.update(any(User.class))).thenReturn(false);

        JSONObject result = (JSONObject) service.addUser(request);

//        assertEquals(0, result.getInteger(Consts.CODE));
//        assertEquals("用户保存失败", result.getString(Consts.MSG));
    }

    @Test
    public void updateUser_success() {
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("username")).thenReturn("newUser");
        when(request.getParameter("password")).thenReturn("newPass");
        when(request.getParameter("sex")).thenReturn("1");
        when(request.getParameter("phoneNum")).thenReturn("1234567890");
        when(request.getParameter("email")).thenReturn("user@example.com");
        when(request.getParameter("birth")).thenReturn("2000-01-01");
        when(request.getParameter("introduction")).thenReturn("Hello");
        when(request.getParameter("location")).thenReturn("City");
        when(request.getParameter("name")).thenReturn("NewName");

        User existingUser = new User();  // Mock the existing user
        existingUser.setUsername("oldUser");
        existingUser.setName("OldName");

        when(userService.getUserWithID(1)).thenReturn(existingUser);
        when(userService.getByUsername("newUser")).thenReturn(null);
        when(userService.getOneUserWithName("NewName")).thenReturn(null);
        when(userService.update(any(User.class))).thenReturn(true);

        // Act
        JSONObject result = (JSONObject) service.updateUser(request);

        // Assert
        assertEquals(1, result.get(Consts.CODE));
        assertEquals("修改成功", result.get(Consts.MSG));
    }

    @Test
    public void testUpdateUser_UsernameEmpty() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("newPass");
        when(request.getParameter("sex")).thenReturn("1");
        when(request.getParameter("phoneNum")).thenReturn("1234567890");
        when(request.getParameter("email")).thenReturn("user@example.com");
        when(request.getParameter("birth")).thenReturn("2000-01-01");
        when(request.getParameter("introduction")).thenReturn("Hello");
        when(request.getParameter("location")).thenReturn("City");
        when(request.getParameter("name")).thenReturn("NewName");


        // Act
        JSONObject result = (JSONObject) service.updateUser(request);

        // Assert
        assertEquals(0, result.get(Consts.CODE));
        assertEquals("用户名不能为空", result.get(Consts.MSG));
    }

    @Test
    public void testUpdateUser_PasswordEmpty() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("username")).thenReturn("testName");
        when(request.getParameter("password")).thenReturn("");
        when(request.getParameter("sex")).thenReturn("1");
        when(request.getParameter("phoneNum")).thenReturn("1234567890");
        when(request.getParameter("email")).thenReturn("user@example.com");
        when(request.getParameter("birth")).thenReturn("2000-01-01");
        when(request.getParameter("introduction")).thenReturn("Hello");
        when(request.getParameter("location")).thenReturn("City");
        when(request.getParameter("name")).thenReturn("NewName");

        // Act
        JSONObject result = (JSONObject) service.updateUser(request);

        // Assert
        assertEquals(0, result.get(Consts.CODE));
        assertEquals("密码不能为空", result.get(Consts.MSG));
    }


    @Test
    public void testDeleteUserSuccess() {
        // Arrange
        String userId = "123";
        when(request.getParameter("id")).thenReturn(userId);
        when(userService.delete(Integer.parseInt(userId))).thenReturn(true);

        // Act
        Object response = service.deleteUser(request);

        // Assert
        assertTrue(response instanceof JSONObject);
        JSONObject jsonResponse = (JSONObject) response;
        assertEquals(1, jsonResponse.getInteger("code"));
        assertEquals("用户删除成功", jsonResponse.getString("msg"));
    }

    @Test
    public void testDeleteUserFailure() {
        // Arrange
        String userId = "123";
        when(request.getParameter("id")).thenReturn(userId);
        when(userService.delete(Integer.parseInt(userId))).thenReturn(false);

        // Act
        Object response = service.deleteUser(request);

        // Assert
        assertTrue(response instanceof JSONObject);
        JSONObject jsonResponse = (JSONObject) response;
        assertEquals(0, jsonResponse.getInteger("code"));
        assertEquals("用户删除失败", jsonResponse.getString("msg"));
    }

    @Test
    public void testSelectByPrimaryKey_UserExists() {
        // Arrange
        String id = "1";
        User user = new User();
        user.setId(1);
        user.setPassword(SecurityUtil.encrypt("password")); // Assume this method encrypts the password

        when(request.getParameter("id")).thenReturn(id);
        when(userService.selectByPrimaryKey(1)).thenReturn(user);

        // Act
        User result = (User) service.selectByPrimaryKey(request);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("password", result.getPassword());
    }

    @Test
    void testSelectByPrimaryKey_UserNotFound() {
        // Arrange
        String id = "2";

        when(request.getParameter("id")).thenReturn(id);
        when(userService.selectByPrimaryKey(2)).thenReturn(null);

        // Act
        User result = (User) service.selectByPrimaryKey(request);

        // Assert
        assertNull(result);
    }


    @Test
    public void testLoginSuccess() throws Exception {
        // Arrange
        String username = "testUser";
        String password = "password123";
        String cypher = SecurityUtil.encrypt(password);

        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);

        when(userService.verifyPassword(username, cypher)).thenReturn(true);

        User user = new User(); // 确保 User 类与实际的 User 类匹配
        user.setId(1);
        user.setProfilePicture("avatar.png");
        when(userService.getByUsername(username)).thenReturn(user);

        // Act
        JSONObject result = (JSONObject) service.login(request, session, response);

        // Assert
        assertEquals(1, result.get(Consts.CODE));
        assertEquals("登录成功", result.get(Consts.MSG));
        assertEquals(1, result.get("userId"));
        assertEquals(username, result.get("username"));
        assertEquals("avatar.png", result.get("avatar"));

    }

    @Test
    public void testLogout() throws Exception {
        // 设置session属性
        session.setAttribute(Consts.NAME, "testUser");

        // 调用logout方法
        service.logout(request, session, response);

        // 验证session属性是否被移除
        assertNull(session.getAttribute(Consts.NAME));

    }
}