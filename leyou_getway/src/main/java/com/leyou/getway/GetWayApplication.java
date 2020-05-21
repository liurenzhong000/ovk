package com.leyou.getway;

import com.netflix.discovery.DiscoveryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import java.util.concurrent.Executors;

@EnableZuulProxy
@SpringCloudApplication
public class GetWayApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(GetWayApplication.class,args);
    }
}
