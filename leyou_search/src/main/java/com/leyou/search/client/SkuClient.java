package com.leyou.search.client;

import com.leyou.item.api.SkuApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("leyou-item")
public interface SkuClient extends SkuApi {
}
