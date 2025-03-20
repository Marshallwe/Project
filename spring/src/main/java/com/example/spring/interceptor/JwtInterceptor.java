package com.example.spring.interceptor;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.spring.constants.RedisConstants;
import com.example.spring.constants.Status;
import com.example.spring.entity.User;
import com.example.spring.exception.BizException;
import com.example.spring.utils.UserHolder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;


@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    RedisTemplate<String, User> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");

        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        if(!StringUtils.hasLength(token)){
            throw  new BizException(Status.TOKEN_ERROR,"token invalid, please log in again");
        }
        User user = redisTemplate.opsForValue().get(RedisConstants.USER_TOKEN_KEY + token);

        if(user == null){
            throw  new BizException(Status.TOKEN_ERROR,"token invalid, please log in again");
        }
        UserHolder.saveUser(user);

        redisTemplate.expire(RedisConstants.USER_TOKEN_KEY +token, RedisConstants.USER_TOKEN_TTL, TimeUnit.MINUTES);

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getUsername())).build();
        try {
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            throw new BizException(Status.TOKEN_ERROR,"token invalid, please log in again");
        }

        return true;
    }
}
