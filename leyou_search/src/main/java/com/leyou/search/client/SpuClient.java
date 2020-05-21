package com.leyou.search.client;

import com.leyou.item.api.SpuApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("leyou-item")
public interface SpuClient extends SpuApi {


}
