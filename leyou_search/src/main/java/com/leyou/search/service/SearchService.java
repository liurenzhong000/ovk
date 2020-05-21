package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.common.vo.PageVO;
import com.leyou.item.pojo.*;
import com.leyou.search.client.*;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchPageVO;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.repository.GoodsRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SkuClient skuClient;

    @Autowired
    private SpecGroupParamClient specGroupParamClient;

    @Autowired
    private SpuClient spuClient;


    /**
     * 将mysql中的商品数据导入到elasticsearch索引库中
     */
    public Goods buildGoods(Spu spu){

        //查询品牌名称
        Brand brand = brandClient.findBrandByBid(spu.getBrandId());
        if(null == brand){
            throw new LyException(ExceptionEnums.BRAND_NOT_FOUND);
        }
        //查询分类名称
        List<Category> categoryList = categoryClient.findCategoryListByIds(Arrays.asList(spu.getCid3(), spu.getCid1(), spu.getCid2()));
        if(CollectionUtils.isEmpty(categoryList)){
            throw new LyException(ExceptionEnums.CATEGORY_CAT_FOUND);
        }
        List<String> cateGoryNameList = categoryList.stream()
                .map(Category::getName)
                .collect(Collectors.toList());
        //查询sku,获取价格集合与sku集合
        List<Sku> skuList = skuClient.findSkusBySpuId(spu.getId());
        //将sku的信息以map的形式存入list中
        List<Map<String,Object>> skuListMap = new ArrayList<>();
        //set集合用于存储sku价格
        Set<Long> priceSet = new HashSet<>();
        skuList.forEach(sku -> {
            priceSet.add(sku.getPrice());
            Map<String,Object> skuMap = new HashMap<>();
            skuMap.put("id",sku.getId());
            skuMap.put("title",sku.getTitle());
            skuMap.put("image",StringUtils.substringBefore(sku.getImages(),","));
            skuMap.put("price",sku.getPrice());
            skuListMap.add(skuMap);
        });
        //获取规格参数
        List<SpecParam> specParamList = specGroupParamClient.querySpecParamListByCid(spu.getCid3(), 1);
        SpuDetail spuDetail = spuClient.findSpuDatailBySpuId(spu.getId());
        Map<Long, String> genericSpecMap = JsonUtils.parseMap(spuDetail.getGenericSpec(), Long.class, String.class);
        Map<Long, List<String>> specialSpecMap = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
        });
        Map<String,Object> specMap =new HashMap<>();
        specParamList.forEach(specParam -> {
            if(specParam.getGeneric().equals(1)){//通用型
                String value = genericSpecMap.get(specParam.getId());
                if(specParam.getNumeric().equals(1)){//数字型，分段
                    value = chooseSegment(value,specParam);
                }
                specMap.put(specParam.getName(),value);

            }else{//特殊性
                specMap.put(specParam.getName(),specialSpecMap.get(specParam.getId()));

            }
        });
        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spu.getId());
        goods.setSubTitle(spu.getSubTitle());
        goods.setAll(spu.getTitle()+" "+brand.getName() + StringUtils.join(cateGoryNameList," "));//分类，品牌，标题等json串
        goods.setPrice(priceSet);
        goods.setSkus(JsonUtils.serialize(skuListMap));
        goods.setSpecs(specMap);
       return goods;

    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    /**
     * 分页查询商品信息
     * @param searchRequest
     * @return
     */
    public PageVO<Goods> findGoodsByPage(SearchRequest searchRequest) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //分页
        nativeSearchQueryBuilder.withPageable(PageRequest.of(searchRequest.getPage()-1,
                searchRequest.getSize()));
        //排序
        nativeSearchQueryBuilder.withSort(SortBuilders
                .fieldSort("createTime")
                .order(SortOrder.DESC));
        //查询
        QueryBuilder all = basicQueryBUilder(searchRequest);
        nativeSearchQueryBuilder.withQuery(all);
        //过滤
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","subTitle","skus"},null));
        //聚合（分类与品牌）
        String categoryAggName = "categoryAgg";
        String brandAggName = "brandAgg";
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders
                .terms(categoryAggName)
                .field("cid3"));
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders
                .terms(brandAggName)
                .field("brandId"));
       // Page<Goods> page = goodsRepository.search(nativeSearchQueryBuilder.build());
        AggregatedPage<Goods> aggregatedPage = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), Goods.class);
        //根据聚合结果获取分类名称与品牌名称
        List<Category> categoryList = parseCategoryAgg(aggregatedPage.getAggregations().get(categoryAggName));
        List<Brand> brandList = parseBrandAgg(aggregatedPage.getAggregations().get(brandAggName));
        //如果查询出的分类总数为1则继续聚合查询对应的规格参数
        List<Map<String,Object>> specs = null;
        if(categoryList != null && categoryList.size() == 1){
            specs = buildSpecAgg(all,categoryList.get(0).getId());
        }
        return new SearchPageVO<>(aggregatedPage.getTotalElements(),aggregatedPage.getTotalPages(),aggregatedPage.getContent(),categoryList,brandList,specs);
    }

    /**
     * 构造基本查询与过滤条件
     * @param searchRequest
     * @return
     */
    private QueryBuilder basicQueryBUilder(SearchRequest searchRequest) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //基本查询
        boolQueryBuilder.must((QueryBuilders.matchQuery("all",searchRequest.getKey()).minimumShouldMatch("")));
        //过滤条件
        Map<String, String> filter = searchRequest.getFilter();
        for (Map.Entry<String, String> enrty : filter.entrySet()) {
            String key = enrty.getKey();
            if(!"cid3".equals(key) && !"brandId".equals(key)){
                key = "specs." + key + ".keyword";
            }
            boolQueryBuilder.filter(QueryBuilders.termQuery(key,enrty.getValue()));
        }
        return boolQueryBuilder;
    }

    /**
     * 如果分类总数为1，查询其对应的规格参数
     * @param all
     * @param id
     * @return
     */
    private List<Map<String, Object>> buildSpecAgg(QueryBuilder all, Long id) {
        List<Map<String,Object>> specs = new ArrayList<>();
        //获取当前分类下所有可搜索的规格参数
        List<SpecParam> specParamList = specGroupParamClient.querySpecParamListByCid(id, 1);
        //基本查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(all);
        //聚合当前分类下所有可搜索的规格参数
        specParamList.forEach(specParam -> nativeSearchQueryBuilder.addAggregation(AggregationBuilders
                .terms(specParam.getName())
                .field("specs."+specParam.getName() + ".keyword")));
        //获取结果
        AggregatedPage<Goods> aggregatedPage = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), Goods.class);
        Aggregations aggs = aggregatedPage.getAggregations();
        specParamList.forEach(specParam -> {
            StringTerms terms = aggs.get(specParam.getName());
            List<String> options = terms.getBuckets().stream().map(StringTerms.Bucket::getKeyAsString).collect(Collectors.toList());
            Map<String,Object> specMap = new HashMap<>();
            specMap.put("specName",specParam.getName());
            specMap.put("options",options);
            specs.add(specMap);
        });
        return specs;
    }

    /**
     * 获取聚合之后的品牌结果
     * @param terms
     * @return
     */
    private List<Brand> parseBrandAgg(LongTerms terms) {
        List<Long> bidList = terms.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue()).collect(Collectors.toList());
        List<Brand> brandList = brandClient.findBrandListByBids(bidList);
        return brandList;
    }

    /**
     * 获取聚合之类的分类结果
     * @param terms
     * @return
     */
    private List<Category> parseCategoryAgg(LongTerms terms) {
        List<Long> cidList = terms.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue()).collect(Collectors.toList());
        List<Category> categoryList = categoryClient.findCategoryListByIds(cidList);
        return categoryList;
    }

    public void createIndex(Long spuId) {
        Goods goods = buildGoods(spuClient.findSpuById(spuId));
        goodsRepository.save(goods);
    }
}
