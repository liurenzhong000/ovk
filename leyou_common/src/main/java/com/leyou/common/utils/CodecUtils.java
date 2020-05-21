package com.leyou.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Author: 98050
 * @Time: 2018-10-23 10:49
 * @Feature: 密码加密
 */
public class CodecUtils {

    public static String passwordBcryptEncode(String username,String password){

        return new BCryptPasswordEncoder().encode(username + password);
    }

    public static Boolean passwordConfirm(String rawPassword,String encodePassword){
        return new BCryptPasswordEncoder().matches(rawPassword,encodePassword);
    }

//    public static void main(String[] args) {
//        System.out.println(new BCryptPasswordEncoder().encode("lll"));
//        //$2a$10$6JKydVrs7IcUfLSFRddVq.EhI4wciD9b6XUrSfvmKkI6UXZzQtaFq
//        //$2a$10$0rreuDvs4.nEe8GPYUjXmuGF7S1U2NiInbYAYS0hlQtzoY9Impqde
//        System.out.println(new BCryptPasswordEncoder().matches("lll","$2a$10$0rreuDvs4.nEe8GPYUjXmuGF7S1U2NiInbYAYS0hlQtzoY9Impqde"));
//    }
}
