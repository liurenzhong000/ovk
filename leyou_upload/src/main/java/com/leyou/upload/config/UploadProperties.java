package com.leyou.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "leyou.upload")
public class UploadProperties {

    private String baseUrl;
    private List<String> allowTypes;
}
