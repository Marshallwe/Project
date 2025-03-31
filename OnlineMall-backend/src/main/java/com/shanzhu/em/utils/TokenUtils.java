package com.shanzhu.em.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.shanzhu.em.constants.Status;
import com.shanzhu.em.entity.User;
import com.shanzhu.em.exception.BizException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


@Component
public class TokenUtils {


    public static String genToken(String userId, String username) {
        String token = JWT.create()
                .withAudience(userId)
                .sign(Algorithm.HMAC256(username));
        return token;
    }

    public static User getCurrentUser() {
        try {
            return UserHolder.getUser();
        } catch (Exception e) {
            return null;
        }
    }


    public static boolean validateLogin() {
        try {
            HttpServletRequest request =
                    ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String token = request.getHeader("token");
            if (StringUtils.hasLength(token)) {
                JWT.decode(token).getAudience();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new BizException(Status.CODE_401, "Login status invalid!");
        }
    }


    public static boolean validateAuthority() {
        try {
            User user = getCurrentUser();
            if (user.getRole().equals("admin")) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        throw new BizException(Status.CODE_403, "No permissions!");
    }

}
