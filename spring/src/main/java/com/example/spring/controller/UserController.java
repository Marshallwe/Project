package com.example.spring.controller;



import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.spring.common.R;
import com.example.spring.constants.Status;
import com.example.spring.entity.form.LoginForm;
import com.example.spring.entity.User;
import com.example.spring.entity.vo.UserVo;
import com.example.spring.service.UserService;
import com.example.spring.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/userinfo/{username}")
    public R<User> getUserInfoByName(@PathVariable String username) {
        return R.success(userService.selectByUsername(username));
    }


    @GetMapping("/userid")
    public long getUserId() {
        return TokenUtils.getCurrentUser().getId();
    }


    @GetMapping("/user/")
    public R<List<User>> findAllUser() {
        return R.success(userService.list());
    }


    @PostMapping("/login")
    public R<UserVo> login(@RequestBody LoginForm loginForm) {
        return R.success(userService.login(loginForm));
    }


    @PostMapping("/register")
    public R<User> register(@RequestBody LoginForm loginForm) {
        return R.success(userService.register(loginForm));
    }


    @PostMapping("/user")
    public R<Void> save(@RequestBody User user) {
        if (BooleanUtil.isTrue(userService.saveOrUpdate(user))) {
            return R.success();
        } else {
            return R.error(Status.CODE_500, "Save failure");
        }
    }


    @DeleteMapping("/user/{id}")
    public R<Void> deleteById(@PathVariable int id) {
        if (BooleanUtil.isTrue(userService.removeById(id))) {
            return R.success();
        } else {
            return R.error(Status.CODE_500, "Deletion failure");
        }
    }


    @PostMapping("/user/del/batch")
    public R deleteBatch(@RequestBody List<Integer> ids) {
        if (BooleanUtil.isTrue(userService.removeBatchByIds(ids))) {
            return R.success();
        } else {
            return R.error(Status.CODE_500, "Deletion failure");
        }
    }


    @GetMapping("/user/page")
    public R<IPage<User>> findUserPage(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            String id,
            String username,
            String nickname
    ) {
        return R.success(userService.selectUserPage(pageNum, pageSize, id, username, nickname));
    }

}
