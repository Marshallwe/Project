package com.example.spring.service;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spring.constants.FileConstants;
import com.example.spring.constants.Status;
import com.example.spring.entity.Avatar;
import com.example.spring.exception.BizException;
import com.example.spring.mapper.AvatarMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;


@Slf4j
@Service
@RequiredArgsConstructor
public class AvatarService extends ServiceImpl<AvatarMapper, Avatar> {

    public String upload(MultipartFile uploadFile) {

        String url;

        InputStream inputStream = null;
        try {
            inputStream = uploadFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String md5 = SecureUtil.md5(inputStream);
        Avatar dbAvatar = lambdaQuery().eq(Avatar::getMd5, md5).one();

        if (dbAvatar == null) {

            String originalFilename = uploadFile.getOriginalFilename();

            String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            long size = uploadFile.getSize() / 1024;

            File folder = new File(FileConstants.AvatarFolderPath);
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

            url = "/avatar/" + finalFileName;

            Avatar avatar = new Avatar();
            avatar.setType(type);
            avatar.setSize(size);
            avatar.setUrl(url);
            avatar.setMd5(md5);
            this.save(avatar);

            return url;
        }

        return dbAvatar.getUrl();
    }


    public void download(String fileName, HttpServletResponse response) {

        File file = new File(FileConstants.AvatarFolderPath + fileName);

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


    public Boolean delete(int id) {

        Avatar avatar = this.getById(id);

        Boolean dbDelResult = this.removeById(id);

        if (BooleanUtil.isTrue(dbDelResult)) {

            String fileName = StrUtil.subAfter(avatar.getUrl(), "/", true);
            File file = new File(FileConstants.AvatarFolderPath + fileName);

            if (file.exists()) {
                boolean fileDelCount = file.delete();
                if (fileDelCount) {
                    log.error("{} file is not exist in directory", id);
                }
            }
        } else {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }


    public Page<Avatar> selectPage(int index, int pageSize) {
        return lambdaQuery().page(new Page<>(index, pageSize));

    }

}