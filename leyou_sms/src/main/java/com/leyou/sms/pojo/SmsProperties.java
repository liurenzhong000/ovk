package com.leyou.sms.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("sms")
@Data
public class SmsProperties {

    private String accessKeyId;
    private String accessSecret;
    private String templateCode;
    private String signName;
    private Integer expire;



}
