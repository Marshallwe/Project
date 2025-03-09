package com.example.spring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spring.entity.User;

public interface UserService extends IService<User> {
    boolean updateUserStatus(Integer userId, boolean status);
    User getUserByAccount(String account);
}