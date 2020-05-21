package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class UploadService {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    /**
     * 上传图片
     * @param file
     * @return
     */
    public String uploadImage(MultipartFile file) throws IOException {
        //获取文件扩展名
        String extension = StringUtils.substringAfterLast(file.getOriginalFilename(),".");
        //上传图片
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
        return storePath.getFullPath();
    }
}
