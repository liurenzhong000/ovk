package com.leyou.getway.config;

import com.leyou.user.util.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;
@Slf4j
@Data
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {

    /**
     * 公钥地址
     */
   // @Value("${ly.jwt.pubKeyPath}")
    private String pubKeyPath;

    /**
     * cookie名称
     */
    //@Value("${ly.jwt.cookieName}")
    private String cookieName;

    /**
     * 公钥
     */
    private PublicKey publicKey;

    /**
     * 项目启动时初始化公钥
     */
    @PostConstruct
    public void init(){
        try {
            PublicKey publicKey =  RsaUtils.getPublicKey(pubKeyPath);
            this.publicKey = publicKey;
        } catch (Exception e) {
            log.error("[网关]初始化公钥失败",e);
            throw new RuntimeException();
        }
    }
}
