package com.example.spring.controller;


import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.spring.common.R;
import com.example.spring.entity.Icon;
import com.example.spring.entity.User;
import com.example.spring.service.IconService;
import com.example.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/icon")
@RequiredArgsConstructor
public class IconController {

    private final IconService iconService;

    private final HttpServletRequest request;

    private final UserService userService;


    @GetMapping("/{id}")
    public R<Icon> findIcon(@PathVariable Long id) {
        return R.success(iconService.getById(id));
    }


    @GetMapping
    public R<List<Icon>> findAllIcon() {
        return R.success(iconService.getIconCategoryMapList());
    }


    @PostMapping
    public R<Void> save(@RequestBody Icon icon) {
        iconService.saveOrUpdate(icon);
        return R.success();
    }


    @PutMapping
    public R<Void> update(@RequestBody Icon icon) {
        iconService.updateById(icon);
        return R.success();
    }


    @GetMapping("/delete")
    public Map<String, Object> delete(@RequestParam("id") Long id) {
        return iconService.deleteById(id);
    }


    public User getUser() {
        String token = request.getHeader("token");
        String username = JWT.decode(token).getAudience().get(0);
        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

}
