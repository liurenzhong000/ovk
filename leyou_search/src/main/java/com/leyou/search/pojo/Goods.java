package com.leyou.search.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @Author: 98050
 * Time: 2018-10-11 17:21
 * Feature:搜索时对应的实体类
 */
@Data
@Document(indexName = "goods", type = "docs", shards = 1, replicas = 0)
public class Goods {
    @Id
    /**
     * spuId
     */
    private Long id;
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    /**
     * 所有需要被搜索的信息，包含标题，分类，甚至品牌
     */
    private String all;
    @Field(type = FieldType.Keyword, index = false)
    /**
     * 卖点
     */
    private String subTitle;
    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 1级分类id
     */
    private Long cid1;
    /**
     * 2级分类id
     */
    private Long cid2;
    /**
     * 3级分类id
     */
    private Long cid3;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 价格
     */
    private Set<Long> price;
    @Field(type = FieldType.Keyword, index = false)
    /**
     * sku信息的json结构
     */
    private String skus;
    /**
     * 可搜索的规格参数，key是参数名，值是参数值
     */
    private Map<String, Object> specs;


}
