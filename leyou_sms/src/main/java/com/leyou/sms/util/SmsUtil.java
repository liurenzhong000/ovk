package com.leyou.sms.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.leyou.common.utils.JsonUtils;
import com.leyou.sms.pojo.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsUtil {


    @Autowired
    private  SmsProperties smsProperties;

    /**
     * 发送短信验证码
     * @param phone 手机号码
     * @param templateParam 模板参数
     * @param templateCode 模板编号
     * @param signName 短信签名
     */
    public  void sendCode(String phone,String templateParam,String templateCode,String signName) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsProperties.getAccessKeyId(), smsProperties.getAccessSecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", templateParam);
        try {
            CommonResponse response = client.getCommonResponse(request);
            if(StringUtils.equalsIgnoreCase("OK",JsonUtils.parseMap(response.getData(), String.class, String.class).get("Code"))){
                log.info("[短信服务]手机号:{}发送验证码成功",phone);
            }
        } catch (ServerException e) {
            log.error("[短信服务]手机号：{}发送短信失败",phone);
        } catch (ClientException e) {
            log.error("[短信服务]手机号：{}发送短信失败",phone);
        }
    }


        }

