package com.example.spring.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spring.constants.FileConstants;
import com.example.spring.constants.Status;
import com.example.spring.entity.SysFile;
import com.example.spring.exception.BizException;
import com.example.spring.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FileService extends ServiceImpl<FileMapper, SysFile> {

    private final FileMapper fileMapper;

    public String upload(MultipartFile uploadFile) {

        String originalFilename = uploadFile.getOriginalFilename();

        String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        long size = uploadFile.getSize() / 1024;

        InputStream inputStream = null;
        try {
            inputStream = uploadFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String md5 = SecureUtil.md5(inputStream);
        List<SysFile> dbSysFileList = lambdaQuery().eq(SysFile::getMd5, md5).list();

        SysFile sysFile = new SysFile();
        sysFile.setName(originalFilename);
        sysFile.setSize(size);
        sysFile.setType(type);

        String url;

        if (dbSysFileList.size() != 0) {
            url = dbSysFileList.get(0).getUrl();
            sysFile.setUrl(url);
        } else {
            File folder = new File(FileConstants.FileFolderPath);
            if (!folder.exists()) {
                folder.mkdir();
            }

            String folderPath = folder.getAbsolutePath() + "/";

            String uuid = UUID.fastUUID().toString();
            String finalFileName = uuid + "." + type;
            File targetFile = new File(folderPath + finalFileName);
            try {
                uploadFile.transferTo(targetFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            url = "/file/" + finalFileName;
            sysFile.setUrl(url);
        }
        sysFile.setMd5(md5);
        fileMapper.insert(sysFile);

        return url;
    }


    public void download(String fileName, HttpServletResponse response) {

        File file = new File(FileConstants.FileFolderPath + fileName);

        if (!file.exists()) {
            throw new BizException(Status.CODE_500, "File does not exist");
        }

        try {
            ServletOutputStream os = response.getOutputStream();
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/octet-stream");
            os.write(FileUtil.readBytes(file));
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Boolean logicDelete(int id) {
        return this.update(
                Wrappers.<SysFile>update()
                        .set("is_delete", Boolean.TRUE)
                        .eq("id", id)
        );
    }


    public Boolean changeEnable(int id, boolean enable) {
        return this.update(
                Wrappers.<SysFile>update()
                        .set("enable", enable)
                        .eq("id", id)
        );
    }


    public IPage<SysFile> selectPage(int pageNum, int pageSize, String fileName) {
        return this.page(
                new Page<>(pageNum, pageSize),
                Wrappers.<SysFile>query()
                        .eq(StrUtil.isNotBlank(fileName), "name", fileName)
                        .eq("is_delete", Boolean.FALSE)
                        .orderByDesc("id"));
    }


}
