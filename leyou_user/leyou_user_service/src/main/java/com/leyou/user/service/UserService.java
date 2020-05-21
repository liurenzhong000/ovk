package com.leyou.user.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.CodecUtils;
import com.leyou.common.utils.Constants;
import com.leyou.common.utils.JsonUtils;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.bo.UserInfo;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.util.JwtProperties;
import com.leyou.user.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JwtProperties jwtProperties;

    @Value("${user.sms.exchange}")
    private String exchange;
    @Value("${user.sms.routingKey}")
    private String routingKey;

    /**
     * 校验手机号码与用户名的唯一性
     * @param type
     * @param data
     * @return
     */
    public Boolean checkDataUnique(Integer type, String data) {
        User user = new User();
        switch (type){
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                throw new  LyException(ExceptionEnums.TYPE_PARAMETER_ERROR);
        }

        return  userMapper.selectCount(user) == 0;
    }

    /**
     * 发送短信验证码
     * @param phone
     */
    public void sendCode(String phone) {
        //生成随机六位数密码
        String code = NumberUtils.generateCode(6);
        //封装phone和code到map
        Map<String,String> map = new HashMap<>();
        map.put("phone",phone);
        map.put("code",code);
        //往mq中发送消息
        try {
            amqpTemplate.convertAndSend(exchange, routingKey, map);
            redisTemplate.opsForValue().set(Constants.USER_VERIFY_CODE+phone,code);
            log.info("[用户服务]向消息队列发送验证码成功,phone:{},code:{}",phone,code);
        }catch (Exception e){
            log.error("[用户服务]发送短信验证码失败，phone:{}",phone);

        }


    }

    /**
     * 用户注册
     * @param user
     * @param code
     */
    public void register(User user, String code) {
        //判断验证码是否正确
        if(!StringUtils.equalsIgnoreCase(code,redisTemplate.opsForValue().get(Constants.USER_VERIFY_CODE+user.getPhone()))){
            throw new LyException(ExceptionEnums.PHONE_CODE_ERROR);
        }
        //对密码加密
        user.setPassword(CodecUtils.passwordBcryptEncode(user.getUsername(),user.getPassword()));
        //注册
        user.setId(null);
        user.setCreated(new Date());
        //如果注册成功,删除redis中的验证码
        if(userMapper.insert(user) == 1) {
            redisTemplate.delete(Constants.USER_VERIFY_CODE + user.getPhone());
        }

    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    public String login(String username, String password) {
        //从缓存中获取用户名与密码，没有则去数据库中查询
        BoundHashOperations<String, Object, Object> boundHashOps = this.redisTemplate.boundHashOps("leyou:user:info");
        String userStr = (String) boundHashOps.get(Constants.USER_SECUROTY + username);
        User user;
        if(StringUtils.isBlank(userStr)){
            user = new User();
            user.setUsername(username);
            user = userMapper.selectOne(user);
            if(user == null){
                throw new LyException(ExceptionEnums.USER_NAME_ORPWD_ERROR);
            }
            //如果用户名与密码都正确，往redis中写入用户信息，尽量减少redis内存占用空间
            if(CodecUtils.passwordConfirm(username+password,user.getPassword())){
                boundHashOps.put(Constants.USER_SECUROTY+username, JsonUtils.serialize(user));
            }
        }else {
            user = JsonUtils.parse(userStr,User.class);
        }
        //判断用户名与密码是否正确
        //
        if(!CodecUtils.passwordConfirm(username + password,user.getPassword())){
            throw new LyException(ExceptionEnums.USER_NAME_ORPWD_ERROR);
        }
        //生成token
        String token = null;
        try {
            token = JwtUtils.generateToken(new UserInfo(user.getId(),user.getUsername()),jwtProperties.getPrivateKey(),jwtProperties.getExpire());
        } catch (Exception e) {
            log.error("[用户服务]生成token失败,user:{}",user.getUsername());
            throw new LyException(ExceptionEnums.SERVER_BUSY_ERROR);
        }
        return token;
    }

    public static void main(String[] args) {
        System.out.println("1".equals(1));
    }
}
