package com.leyou.item.service;

import com.leyou.common.utils.JsonUtils;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SkuService {

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    /**
     * 根据spuId查询sku列表
     * @param spuId
     * @return
     */
    public List<Sku> findSkusBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        return skuMapper.select(sku);
    }

    /**
     * 通过ids查询sku信息
     * @param skuIds
     * @return
     */
    public List<Sku> querySkusByIds(List<Long> skuIds) {
        List<Sku> skuList = skuMapper.selectByIdList(skuIds);
        Map<Long, Stock> stockMap = JsonUtils.parseMap(
                JsonUtils.serialize(stockMapper.selectByIdList(skuList.stream().map(Sku::getId).collect(Collectors.toList()))),
                Long.class,
                Stock.class);
        skuList.forEach(sku ->{
            sku.setStock(((Stock)stockMap.get(sku.getId())).getStock());
        });
        return skuList;
    }
}
