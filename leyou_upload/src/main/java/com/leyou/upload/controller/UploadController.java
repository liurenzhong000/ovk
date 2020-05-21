package com.leyou.upload.controller;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.upload.config.UploadProperties;
import com.leyou.upload.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("upload")
@EnableConfigurationProperties(UploadProperties.class)
@Slf4j
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @Autowired
    private UploadProperties uploadProperties;

    /**
     * 上传图片
     * @param file
     * @return
     */
    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile file){
        String storePath = null;
        /**
         * 判断上传的图片是否合法
         */
        try {
            //文件类型是否支持
            if(! uploadProperties.getAllowTypes().contains(file.getContentType())){
                log.error("文件类型不支持");
                throw new LyException(ExceptionEnums.FILE_TYPE_ERROR);
            }
            //是否为图片
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if(bufferedImage == null){
                log.error("文件内容不符合要求");
                throw new LyException(ExceptionEnums.FILE_CONTENT_ERROR);
            }
            /**
             * 上传图片到Fastdfs服务器
             */
             storePath = uploadService.uploadImage(file);
        } catch (IOException e) {
            log.error("文件上传失败");
            throw new LyException(ExceptionEnums.FILE_UPLOAD_FAIL);
        }

        return ResponseEntity.ok(uploadProperties.getBaseUrl() + storePath);
    }


}
