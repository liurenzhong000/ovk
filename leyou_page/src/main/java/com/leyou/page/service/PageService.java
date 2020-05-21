package com.leyou.page.service;

import com.leyou.item.pojo.*;
import com.leyou.page.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PageService {

    @Autowired
    private SpuClient spuClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private SkuClient skuClient;

    @Autowired
    private SpecGroupParamClient specGroupParamClient;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 获取模型数据，实现页面静态化
     * @param id
     * @return
     */
    public Map<String, Object> generateItemHtml(Long id) {
        Map<String,Object> itemMap = new HashMap<>();

        //获取spu的标题与子标题
        Spu spu = spuClient.findSpuById(id);
        if(spu != null){
            itemMap.put("title",spu.getTitle());
            itemMap.put("subTitle",spu.getSubTitle());
        }
        //获取多级分类名称
        List<Category> categories = categoryClient.findCategoryListByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //获取品牌名称
        Brand brand = brandClient.findBrandByBid(spu.getBrandId());
        //获取skus
        List<Sku> skus = skuClient.findSkusBySpuId(id);
        //获取规格参数
        List<SpecGroup> specs = specGroupParamClient.findSpecGroups(spu.getCid3());
        //获取detail
        SpuDetail detail = spuClient.findSpuDatailBySpuId(spu.getId());

        itemMap.put("categories",categories);
        itemMap.put("brand",brand);
        itemMap.put("skus",skus);
        itemMap.put("specs",specs);
        itemMap.put("detail",detail);

        //页面静态化
        pageStatic(spu.getId(),itemMap);
        return itemMap;
    }

    /**
     * 页面静态化
     * @param spuId
     * @param itemMap
     */
    private void pageStatic(Long spuId,Map<String, Object> itemMap) {
        Context context = new Context();
        context.setVariables(itemMap);
        File file = new File("/Users/liurenzhong",spuId+".html");
        if(file.exists()){
            file.delete();
        }
        try(PrintWriter writer = new PrintWriter(file,"UTF-8")) {
            templateEngine.process("item",context,writer);
        } catch (Exception e) {
            log.error("[页面静态化失败]",e);
        }
    }
}
