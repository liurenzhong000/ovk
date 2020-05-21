package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.utils.JsonUtils;
import com.leyou.common.vo.PageVO;
import com.leyou.item.pojo.Spu;
import com.leyou.search.client.SpuClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchServiceTest {

    @Autowired
    private SearchService searchService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private SpuClient spuClient;

    @Autowired
    private GoodsRepository goodsRepository;

    public static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void createIndex(){
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);
    }

    @Test
    public void buildGoods() throws IOException {
        PageVO<Spu> pageVO = spuClient.findSpuByPage(1, 200, null, true);
        List<Spu> dataList = pageVO.getItems();
        List<Spu> myObjects =
                mapper.readValue(JsonUtils.serialize(dataList), new TypeReference<List<Spu>>(){});
        List<Goods> goodsList = myObjects.stream().map(searchService::buildGoods).collect(Collectors.toList());

        goodsRepository.saveAll(goodsList);

    }

    @Test
    public void test(){
        Long i= new Long(1);
        System.out.println(i.equals(1L));
    }
}
