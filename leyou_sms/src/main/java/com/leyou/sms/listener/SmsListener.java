package com.leyou.sms.listener;

import com.leyou.common.utils.Constants;
import com.leyou.common.utils.JsonUtils;
import com.leyou.sms.pojo.SmsProperties;
import com.leyou.sms.util.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {

    @Autowired
    private SmsProperties sms;

    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 监听队列中的消息，发送短信验证码
     * @param dataMap
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "sms.verify.code.queue",durable = "true"),
            exchange = @Exchange(value = "ly.sms.exchange",type = ExchangeTypes.TOPIC),
            key = "sms.verify.code"))
    public void smsListen(Map<String,String> dataMap){
        if(CollectionUtils.isEmpty(dataMap)){
            return;
        }
        String phone = dataMap.remove("phone");
        String code = dataMap.get("code");
        if(StringUtils.isBlank(phone) || StringUtils.isBlank(code)){
            return;
        }
        smsUtil.sendCode(phone,JsonUtils.serialize(dataMap),sms.getTemplateCode(),sms.getSignName());
        //短信验证码发送成功后将验证码存入到reids中,有效时长为5分钟
        redisTemplate.opsForValue().set(Constants.USER_VERIFY_CODE+phone,code,sms.getExpire(), TimeUnit.MINUTES);

    }
}
