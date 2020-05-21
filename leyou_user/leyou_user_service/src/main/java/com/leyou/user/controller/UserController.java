package com.leyou.user.controller;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.CookieUtils;
import com.leyou.user.bo.UserInfo;
import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import com.leyou.user.util.JwtProperties;
import com.leyou.user.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties properties;

    /**
     * 校验用户名与手机号码的唯一性
     * @param type
     * @param data
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkDataUnique(@PathVariable("data")String data,@PathVariable("type")Integer type){
        return ResponseEntity.ok(userService.checkDataUnique(type,data));
    }

    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    @PostMapping("send")
    public ResponseEntity<Void> send(@RequestParam("phone") String phone){
        userService.sendCode(phone);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 用户注册
     * @param user
     * @param code
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user,@RequestParam("code")String code){
        //判断用户名与手机号是否唯一
        if(!userService.checkDataUnique(1,user.getUsername())){
            throw new LyException(ExceptionEnums.USER_NAME_ALREADY_EXISTS_ERROR);
        }else if(!userService.checkDataUnique(2,user.getPhone())){
            throw new LyException(ExceptionEnums.PHONE_ALREADY_REGISTER_ERROR);
        }
        //注册
        userService.register(user,code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<Void> login(@RequestParam("username")String username,
                                      @RequestParam("password")String password,
                                      HttpServletRequest request,
                                      HttpServletResponse response){
        String token = userService.login(username,password);
        //2.将token写入cookie，并指定httpOnly为true，防止通过js获取和修改
        CookieUtils.setCookie(request,response,properties.getCookieName(),token,properties.getCookieMaxAge(),true);
        return ResponseEntity.ok().build();

    }

    /**
     * 验证用户是否登录
     * @param token
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("LY_TOKEN") String token,HttpServletResponse response,HttpServletRequest request){
        try {
            //验证token，获取用户信息
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, properties.getPublicKey());
            //重新生成token
            token = JwtUtils.generateToken(userInfo, properties.getPrivateKey(), properties.getExpire());
            //将新生成的token写入cookie
            CookieUtils.setCookie(request,response,properties.getCookieName(),token,properties.getCookieMaxAge());
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            //解析token失败
            throw new LyException(ExceptionEnums.NOT_LOGIN);
        }
    }
}
