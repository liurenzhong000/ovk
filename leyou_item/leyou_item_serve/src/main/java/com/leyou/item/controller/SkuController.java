package com.leyou.item.controller;

import com.leyou.item.pojo.Sku;
import com.leyou.item.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("sku")
public class SkuController {

    @Autowired
    private SkuService skuService;

    /**
     * 根据spuId查询sku列表
     * @param spuId
     * @return
     */
    @GetMapping("{spuId}")
    public ResponseEntity<List<Sku>> findSkusBySpuId(@PathVariable("spuId")Long spuId){
        List<Sku> skuList = skuService.findSkusBySpuId(spuId);
        return ResponseEntity.ok(skuList);
    }

    /**
     * 通过ids查询sku信息
     * @param skuIds
     * @return
     */
    @GetMapping("carts")
    public ResponseEntity<List<Sku>> querySkusByIds(@RequestParam("ids")List<Long> skuIds){
        List<Sku> skuList = skuService.querySkusByIds(skuIds);
        return ResponseEntity.ok(skuList);
    }

}
