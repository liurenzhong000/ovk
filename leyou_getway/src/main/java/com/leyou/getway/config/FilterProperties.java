package com.leyou.getway.config;

import com.leyou.user.util.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.RSAUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.security.PublicKey;
import java.util.List;

@Data
//@Configuration
@Slf4j
@ConfigurationProperties("ly.filter")
public class FilterProperties {


    //@Value("${ly.filter.allowPaths}")
    private List<String> allowPaths;


}
