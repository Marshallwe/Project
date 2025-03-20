package com.example.spring.controller;


import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.spring.common.R;
import com.example.spring.constants.Status;
import com.example.spring.entity.SysFile;
import com.example.spring.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;


    @PostMapping("/upload")
    public R<String> upload(@RequestParam MultipartFile file) {
        return R.success(fileService.upload(file));
    }

    @GetMapping("/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        fileService.download(fileName, response);
    }

    @DeleteMapping("/{id}")
    public R<Void> deleteById(@PathVariable Integer id) {

        if (BooleanUtil.isTrue(fileService.logicDelete(id))) {
            return R.success();
        } else {
            return R.error(Status.CODE_500, "Failed file deletion");
        }
    }


    @PostMapping("/del/batch")
    public R<Void> deleteBatch(@RequestBody List<Integer> ids) {
        for (Integer id : ids) {
            if (BooleanUtil.isFalse(fileService.logicDelete(id))) {
                return R.error(Status.CODE_500, "Deleting files：" + fileService.getById(id).getName() + "时失败，删除已停止");
            }
        }
        return R.success();
    }

    @GetMapping("/enable")
    public R<Void> changeEnable(@RequestParam int id, @RequestParam boolean enable) {
        if (BooleanUtil.isTrue(fileService.changeEnable(id, enable))) {
            return R.error(Status.CODE_500, "File status update failed");
        } else {
            return R.success();
        }
    }


    @GetMapping("/page")
    public R<IPage<SysFile>> findFilePage(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(required = false) String fileName
    ) {
        return R.success(fileService.selectPage(pageNum, pageSize, fileName));
    }

}
