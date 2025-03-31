package com.shanzhu.em.controller;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shanzhu.em.common.R;
import com.shanzhu.em.constants.Status;
import com.shanzhu.em.entity.Avatar;
import com.shanzhu.em.service.AvatarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/avatar")
@RequiredArgsConstructor
public class AvatarController {

    private final AvatarService avatarService;

    @PostMapping()
    public R<String> uploadAvatar(@RequestParam MultipartFile file) {
        return R.success(avatarService.upload(file));
    }

    @GetMapping("/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        avatarService.download(fileName, response);
    }


    @DeleteMapping("/{id}")
    public R<Void> deleteById(@PathVariable int id) {
        if (BooleanUtil.isTrue(avatarService.delete(id))) {
            return R.success();
        } else {
            return R.error(Status.CODE_500, "Deletion failure");
        }
    }


    @GetMapping("/page")
    public R<Map<String, String>> findUserAvatarPage(@RequestParam int pageNum, @RequestParam int pageSize) {
        Page<Avatar> page = avatarService.selectPage((pageNum - 1) * pageSize, pageSize);
        HashMap<String, Object> map = new HashMap<>();
        map.put("records", page.getRecords());
        map.put("total", page.getTotal());
        return R.success(map);
    }

}
