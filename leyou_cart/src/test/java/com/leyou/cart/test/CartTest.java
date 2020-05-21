package com.leyou.cart.test;

import com.google.inject.internal.cglib.proxy.$Callback;
import com.leyou.cart.CartApplication;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.utils.Constants;
import com.leyou.common.utils.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = CartApplication.class)
public class CartTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void addCart(){
        Cart cart = new Cart();
        cart.setNum(1);
        cart.setImage("http://image.leyou.com/images/9/15/1524297313793.jpg");
        //cart.setOwnSpec();
        cart.setPrice(84900L);
        cart.setSkuId(2600242L);
        cart.setTitle("华为 G9 青春版 白色 移动联通电信4G手机 双卡双待");
        cart.setUserId(32L);
        redisTemplate.boundHashOps(Constants.USER_CART + 32).put("2600242", JsonUtils.serialize(cart));

    }
}
