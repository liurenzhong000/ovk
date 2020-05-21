package com.leyou.item.test;


import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class ItemTest {

    @Test
    public void t1(){
        try{
            //int i = 1/0;
            //System.out.println("测试异常");
            System.out.println(StringUtils.isBlank(" a"));
        }catch (Exception e){

        }
    }
}
