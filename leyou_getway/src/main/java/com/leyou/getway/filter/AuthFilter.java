package com.leyou.getway.filter;

import com.leyou.common.utils.CookieUtils;
import com.leyou.getway.config.FilterProperties;
import com.leyou.getway.config.JwtProperties;
import com.leyou.user.bo.UserInfo;
import com.leyou.user.util.JwtUtils;
import com.netflix.zuul.FilterProcessor;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 登录认证过滤器
 */
@Slf4j
@Component
@EnableConfigurationProperties({FilterProperties.class,JwtProperties.class})
public class AuthFilter extends ZuulFilter{

    @Autowired
    private FilterProperties filterProp;

    @Autowired
    private JwtProperties jwtProp;
    /**
     * 过滤器类型
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 过滤器执行顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER-1;
    }

    /**
     * 是否开启过滤器
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return !isAllow();
    }

    private boolean isAllow() {
        //获取当前地址URI
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        String URI = request.getRequestURI();
        log.info("[网关]URI:",URI);
        List<String> allowPaths = filterProp.getAllowPaths();
        for (String allowPath : allowPaths) {
            if(StringUtils.startsWith(URI,allowPath)){
                return true;
            }
        }
        return false;
    }

    /**
     * 要执行的逻辑
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {

        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        //获取token
        String token = CookieUtils.getCookieValue(request, jwtProp.getCookieName());
        //解析token
        try {
            //解析token
            JwtUtils.getInfoFromToken(token, jwtProp.getPublicKey());
        } catch (Exception e) {
            log.error("[网关]解析token失败,token:{}",token);
            //解析token失败，未登录，拦截
            currentContext.setSendZuulResponse(false);
            //返回状态码
            currentContext.setResponseStatusCode(HttpStatus.FORBIDDEN.value());


        }

        return null;
    }
}
