package com.example.spring.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.spring.Result;
import com.example.spring.entity.User;
import com.example.spring.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Objects;

@Api(tags = "User Management")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("Create User")
    @PostMapping
    public Result createUser(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return Result.error(result.getAllErrors().get(0).getDefaultMessage());
        }
        if (userService.getUserByAccount(user.getNo()) != null) {
            return Result.error("Account already exists");
        }
        return Result.success(userService.save(user));
    }

    @ApiOperation("Get User by ID")
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable Integer id) {
        User user = userService.getById(id);
        return user != null ? Result.success(user) : Result.error("User not found");
    }

    @ApiOperation("Update User")
    @PutMapping("/{id}")
    public Result updateUser(@PathVariable Integer id, @RequestBody User user) {
        user.setId(id);
        return Result.success(userService.updateById(user));
    }
    @ApiOperation("Delete User")
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Integer id) {
        return Result.success(userService.removeById(id));
    }
    @ApiOperation("Search Users with Pagination")
    @GetMapping
    public Result searchUsers(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<User> pageInfo = new Page<>(page, size);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (!keyword.isEmpty()) {
            wrapper.like("name", keyword).or().like("no", keyword);
        }
        return Result.success(userService.page(pageInfo, wrapper));
    }

    @ApiOperation("Update User Status")
    @PatchMapping("/{id}/status")
    public Result updateStatus(
            @PathVariable Integer id,
            @RequestParam Boolean status) {
        return Result.success(userService.updateUserStatus(id, status));
    }
    @ApiOperation("Change the password")
    @PatchMapping("/{id}/password")
    public Result updatePassword(
            @PathVariable Integer id,
            @RequestBody java.util.Map<String, String> passwordMap) {


        String oldPassword = passwordMap.get("oldPassword");
        String newPassword = passwordMap.get("newPassword");
        if (oldPassword == null || newPassword == null) {
            return Result.error("it can't be blankspace");
        }

        User user = userService.getById(id);
        if (user == null) {
            return Result.error("user don't exist");
        }

        if (!Objects.equals(user.getPassword(), oldPassword)) {
            return Result.error("old password error");
        }

        user.setPassword(newPassword);
        boolean success = userService.updateById(user);
        return success ? Result.success() : Result.error("fail to update password");
    }

}