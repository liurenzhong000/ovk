package com.leyou.user.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Author: 98050
 * @Time: 2018-10-23 22:20
 * @Feature: jwt配置参数
 */
@Slf4j
@Data
//@ConfigurationProperties(prefix = "leyou.jwt")
@Configuration
@RefreshScope
public class JwtProperties {

    /**
     * 密钥
     */
    @Value("${leyou.jwt.secret}")
    private String secret;

    /**
     * 公钥地址
     */
    @Value("${leyou.jwt.pubKeyPath}")
    private String pubKeyPath;

    /**
     * 私钥地址
     */
    @Value("${leyou.jwt.priKeyPath}")
    private String priKeyPath;

    /**
     * token过期时间
     */
    @Value("${leyou.jwt.expire}")
    private int expire;

    /**
     * 公钥
     */
    private PublicKey publicKey;

    /**
     * 私钥
     */
    private PrivateKey privateKey;

    /**
     * cookie名字
     */
    @Value("${leyou.jwt.cookieName}")
    private String cookieName;

    /**
     * cookie生命周期
     */
    @Value("${leyou.jwt.cookieMaxAge}")
    private Integer cookieMaxAge;


    /**
     * @PostConstruct :在构造方法执行之后执行该方法
     */
    @PostConstruct
    public void init(){
        try {
            File pubKey = new File(pubKeyPath);
            File priKey = new File(priKeyPath);
            if (!pubKey.exists() || !priKey.exists()) {
                // 生成公钥和私钥
                RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
            }
            // 获取公钥和私钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
        } catch (Exception e) {
            log.error("初始化公钥和私钥失败！", e);
            throw new RuntimeException();
        }
    }
}
