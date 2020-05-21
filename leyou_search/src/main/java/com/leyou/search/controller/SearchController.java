package com.leyou.search.controller;

import com.leyou.common.vo.PageVO;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 分页查询商品信息
     * @param searchRequest
     * @return
     */
    @PostMapping("page")
    public ResponseEntity<PageVO> findGoodsByPage(@RequestBody SearchRequest searchRequest){
        PageVO<Goods> pageVO = searchService.findGoodsByPage(searchRequest);

        return ResponseEntity.ok(pageVO);
    }


}
