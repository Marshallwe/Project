package com.example.spring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spring.entity.User;
import com.example.spring.mapper.UserMapper;
import com.example.spring.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl
        extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Override
    public boolean updateUserStatus(Integer userId, boolean status) {
        User user = new User();
        user.setId(userId);
        user.setValid(status);
        return updateById(user);
    }

    @Override
    public User getUserByAccount(String account) {
        return baseMapper.selectByAccount(account);
    }
}