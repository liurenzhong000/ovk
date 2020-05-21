package com.leyou.search.client;


import com.leyou.item.api.SpecGroupParamApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("leyou-item")
public interface SpecGroupParamClient extends SpecGroupParamApi {
}
