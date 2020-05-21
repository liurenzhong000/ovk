package com.leyou.item.api;

import com.leyou.item.pojo.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("brand")
public interface BrandApi {

    /**
     * 根据品牌ID查询品牌
     * @param bid
     * @return
     */
    @GetMapping("{bid}")
     Brand findBrandByBid(@PathVariable("bid")Long bid);

    /**
     * 根据bids批量查询品牌信息
     * @param bids
     * @return
     */
    @GetMapping("brands")
    List<Brand> findBrandListByBids(@RequestParam List<Long> bids);

}
