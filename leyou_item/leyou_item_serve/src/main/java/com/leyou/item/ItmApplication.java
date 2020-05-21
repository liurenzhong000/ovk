package com.leyou.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.leyou")
@MapperScan("com.leyou.item.mapper")
public class ItmApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItmApplication.class,args);
    }
}
