package com.leyou.item.api;

import com.leyou.item.pojo.Sku;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("sku")
public interface SkuApi {
    /**
     * 根据spuId查询sku列表
     * @param spuId
     * @return
     */
    @GetMapping("{spuId}")
    List<Sku> findSkusBySpuId(@PathVariable("spuId")Long spuId);
}
