package com.shanzhu.em.controller;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shanzhu.em.common.R;
import com.shanzhu.em.constants.Status;
import com.shanzhu.em.entity.User;
import com.shanzhu.em.entity.form.LoginForm;
import com.shanzhu.em.entity.vo.UserVo;
import com.shanzhu.em.service.UserService;
import com.shanzhu.em.utils.TokenUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getUserInfoByName_ValidUsername_ReturnsUserInfo() {
        String testUsername = "testUser";
        User mockUser = new User();
        mockUser.setUsername(testUsername);

        when(userService.selectByUsername(testUsername)).thenReturn(mockUser);

        R<User> result = userController.getUserInfoByName(testUsername);

        assertEquals(Status.CODE_200, result.getCode());
        assertEquals(testUsername, result.getData().getUsername());
        verify(userService).selectByUsername(testUsername);
    }


    @Test
    void findAllUser_ReturnsUserList() {
        List<User> mockUsers = Arrays.asList(
                createTestUser(1, "user1"),
                createTestUser(2, "user2")
        );

        when(userService.list()).thenReturn(mockUsers);

        R<List<User>> result = userController.findAllUser();

        assertEquals(2, result.getData().size());
        verify(userService).list();
    }

    @Test
    void login_ValidCredentials_ReturnsUserVo() {
        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("testUser");
        loginForm.setPassword("123456");

        UserVo mockUserVo = new UserVo();
        mockUserVo.setUsername("testUser");

        when(userService.login(loginForm)).thenReturn(mockUserVo);

        R<UserVo> result = userController.login(loginForm);

        assertEquals(Status.CODE_200, result.getCode());
        assertEquals("testUser", result.getData().getUsername());
        verify(userService).login(loginForm);
    }

    @Test
    void register_NewUser_ReturnsRegisteredUser() {
        LoginForm registerForm = new LoginForm();
        registerForm.setUsername("newUser");
        registerForm.setPassword("654321");

        User mockUser = createTestUser(3, "newUser");
        when(userService.register(registerForm)).thenReturn(mockUser);

        R<User> result = userController.register(registerForm);

        assertEquals(Status.CODE_200, result.getCode());
        assertEquals("newUser", result.getData().getUsername());
        verify(userService).register(registerForm);
    }

    @Test
    void saveUser_Success_ReturnsSuccess() {
        User testUser = createTestUser(4, "saveUser");
        when(userService.saveOrUpdate(testUser)).thenReturn(true);

        R<Void> result = userController.save(testUser);

        assertEquals(Status.CODE_200, result.getCode());
        verify(userService).saveOrUpdate(testUser);
    }


    @Test
    void deleteById_ValidId_ReturnsSuccess() {
        when(userService.removeById(100)).thenReturn(true);

        R<Void> result = userController.deleteById(100);

        assertEquals(Status.CODE_200, result.getCode());
        verify(userService).removeById(100);
    }

    @Test
    void deleteBatch_ValidIds_ReturnsSuccess() {
        List<Integer> ids = Arrays.asList(101, 102, 103);
        when(userService.removeBatchByIds(ids)).thenReturn(true);

        R<Void> result = userController.deleteBatch(ids);

        assertEquals(Status.CODE_200, result.getCode());
        verify(userService).removeBatchByIds(ids);
    }

    @Test
    void findUserPage_WithFilters_ReturnsPagedData() {
        int pageNum = 1;
        int pageSize = 10;
        IPage<User> mockPage = new Page<>(pageNum, pageSize);
        mockPage.setRecords(Arrays.asList(
                createTestUser(6, "pageUser1"),
                createTestUser(7, "pageUser2")
        ));

        when(userService.selectUserPage(eq(pageNum), eq(pageSize), anyString(), anyString(), anyString()))
                .thenReturn(mockPage);

        R<IPage<User>> result = userController.findUserPage(pageNum, pageSize, "1", "user", "nick");

        assertEquals(2, result.getData().getRecords().size());
        verify(userService).selectUserPage(pageNum, pageSize, "1", "user", "nick");
    }

    private User createTestUser(int id, String username) {
        User user = new User();
        user.setId((int) id);
        user.setUsername(username);
        user.setNickname("Test User");
        user.setPassword("encodedPassword");
        user.setRole("USER");
        return user;
    }
}