package com.leyou.cart.service;

import com.leyou.cart.interceptor.CartInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.Constants;
import com.leyou.common.utils.JsonUtils;
import com.leyou.user.bo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 加入购物车
     * @param cart
     */
    public void addCart(Cart cart) {
        //1.获取用户信息
        UserInfo userInfo = CartInterceptor.get();
         Integer num = cart.getNum();
        //2.该商品是否已经在该用户的购物车中
        BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(Constants.USER_CART + userInfo.getId());
        String hashKey = cart.getSkuId().toString();
        //2.1在，新增数量
        if(boundHashOps.hasKey(hashKey)){
            cart = JsonUtils.parse(boundHashOps.get(hashKey).toString(), Cart.class);
            cart.setNum(cart.getNum() + num);
        }
        //2.2不在，加入缓存
        boundHashOps.put(hashKey,JsonUtils.serialize(cart));
    }

    /**
     * 查询购物车列表
     * @return
     */
    public List<Cart> queryCartListByUid() {
        //获取用户信息
       // UserInfo userInfo = CartInterceptor.get();
        String key  = Constants.USER_CART + CartInterceptor.get().getId();
        if(!redisTemplate.hasKey(key)){
            return null;
        }
        BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(key);
        List<Cart> cartList = boundHashOps.values().stream().map(o -> JsonUtils.parse(o.toString(), Cart.class)).collect(Collectors.toList());
        return cartList;

    }

    /**
     * 删除购物车
     * @param ids
     */
    public void removeCartById(List<Long> ids) {
        UserInfo userInfo = CartInterceptor.get();
        String key = Constants.USER_CART + userInfo.getId();
        if(!redisTemplate.hasKey(key)){
            throw new LyException(ExceptionEnums.CART_NOT_FOUNT);
        }
        BoundHashOperations<String, Object, Object> boundHashOperations = redisTemplate.boundHashOps(key);
        boundHashOperations.delete(ids);

    }
}
