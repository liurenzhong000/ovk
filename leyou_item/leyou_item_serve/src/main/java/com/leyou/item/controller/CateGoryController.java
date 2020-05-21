package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CateGoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("category")
public class CateGoryController {

    @Autowired
    private CateGoryService cateGoryService;


    /**
     * 根据父节点查询商品类目
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> findCategoryListByPid(@RequestParam(value = "pid",required = true)Long pid){

        List<Category> categoryList = cateGoryService.findCategoryListByPid(pid);
        return ResponseEntity.ok(categoryList);


    }

    /**
     * 根据ids批量查询商品类目
     * @param ids
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Category>> findCategoryListByIds(@RequestParam List<Long> ids){
        List<Category> categoryList = cateGoryService.findCategoryListByIds(ids);
        return ResponseEntity.ok(categoryList);
    }
}
