package com.leyou.cart.interceptor;

import com.leyou.cart.properties.JwtProperties;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.CookieUtils;
import com.leyou.user.bo.UserInfo;
import com.leyou.user.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CartInterceptor implements HandlerInterceptor {

    private JwtProperties jwtProp;

    public CartInterceptor(JwtProperties jwtProp){this.jwtProp = jwtProp;}

    public static final  ThreadLocal<UserInfo> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String token = CookieUtils.getCookieValue(request, jwtProp.getCookieName());
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProp.getPublicKey());
            threadLocal.set(userInfo);
            return true;
        }catch (Exception e){
            throw new LyException(ExceptionEnums.NOT_LOGIN);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        threadLocal.remove();;
    }

    public static UserInfo get(){
        return threadLocal.get();
    }
}
