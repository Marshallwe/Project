package com.shanzhu.em.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.em.constants.RedisConstants;
import com.shanzhu.em.constants.Status;
import com.shanzhu.em.entity.form.LoginForm;
import com.shanzhu.em.entity.User;
import com.shanzhu.em.entity.vo.UserVo;
import com.shanzhu.em.exception.BizException;
import com.shanzhu.em.mapper.UserMapper;
import com.shanzhu.em.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Resource
    RedisTemplate<String, User> redisTemplate;

    public User selectByUsername(String username) {
        return lambdaQuery().eq(User::getUsername, username).one();
    }


    public IPage<User> selectUserPage(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            String id,
            String username,
            String nickname
    ) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(id)) {
            userQueryWrapper.like("id", id);
        }
        if (StrUtil.isNotBlank(username)) {
            userQueryWrapper.like("username", username);
        }
        if (StrUtil.isNotBlank(nickname)) {
            userQueryWrapper.like("nickname", nickname);
        }
        userQueryWrapper.orderByDesc("id");

        return this.page(new Page<>(pageNum, pageSize), userQueryWrapper);
    }


    public UserVo login(LoginForm loginForm) {
        User user = lambdaQuery()
                .eq(User::getUsername, loginForm.getUsername())
                .eq(User::getPassword, loginForm.getPassword())
                .one();

        if (user == null) {
            throw new BizException(Status.CODE_403, "Wrong username or password");
        }

        String token = TokenUtils.genToken(user.getId().toString(), user.getUsername());

        redisTemplate.opsForValue().set(RedisConstants.USER_TOKEN_KEY + token, user);

        redisTemplate.expire(RedisConstants.USER_TOKEN_KEY + token, RedisConstants.USER_TOKEN_TTL, TimeUnit.MINUTES);

        UserVo userVo = BeanUtil.copyProperties(user, UserVo.class);
        userVo.setToken(token);

        return userVo;
    }

    public User register(LoginForm loginForm) {
        User user = lambdaQuery()
                .eq(User::getUsername, loginForm.getUsername())
                .one();

        if (user != null) {
            throw new BizException(Status.CODE_403, "The username has been used");
        } else {
            user = new User();
            BeanUtils.copyProperties(loginForm, user);
            user.setNickname("new_user");
            user.setRole("user");
            this.save(user);
            return user;
        }
    }

}
