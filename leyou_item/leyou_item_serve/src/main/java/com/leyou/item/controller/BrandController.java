package com.leyou.item.controller;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageVO;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 分页查询品牌信息
     * @param curPage
     * @param pageSize
     * @param key
     * @param orderBy
     * @param desc
     * @return
     */
    @GetMapping("page/list")
    public ResponseEntity<PageVO<Brand>> findBrandListByPage(@RequestParam(value = "curPage",defaultValue = "1")Integer curPage,
                                                             @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                                                             @RequestParam(value = "key",required = false)String key,
                                                             @RequestParam(value = "orderBy",defaultValue = "id")String orderBy,
                                                             @RequestParam(value = "desc",defaultValue = "false")Boolean desc){
        PageVO<Brand> pageVO = brandService.findBrandListByPage(curPage,pageSize,key,orderBy,desc);

        return ResponseEntity.ok(pageVO);
    }

    /**
     * 新增品牌
     * @param brand
     * @param cids
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addBrand(Brand brand,@RequestParam(value = "cids") List<Long> cids){
        //判断条件非空
        /*if(brand != null) {
            if (StringUtils.isBlank(brand.getName().trim())) {
                throw new LyException(ExceptionEnums.BRAND_NAME_CANT_BE_NULL);
            }
            if (StringUtils.isBlank(brand.getImage().trim())) {
                throw new LyException(ExceptionEnums.BRAND_IMAGE_CANT_BE_NULL);
            }
            if(StringUtils.isBlank(brand.getLetter().toString().trim())){
                throw new LyException(ExceptionEnums.BRAND_LETTER_CANT_BE_NULL);
            }
        }
        if(CollectionUtils.isEmpty(cids)){
            throw new LyException(ExceptionEnums.BRAND_BLONG_CATEGORY_ID);
        }*/
        //新增品牌
        brandService.addBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据分类ID查询品牌列表
     * @return
     */
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandListByCid(@PathVariable("cid")Long cid){
        List<Brand> brandList = brandService.queryBrandListByCid(cid);
        return ResponseEntity.ok(brandList);
    }

    /**
     * 根据品牌ID查询品牌
     * @param bid
     * @return
     */
    @GetMapping("{bid}")
    public ResponseEntity<Brand> findBrandByBid(@PathVariable("bid")Long bid){
        Brand brand = brandService.findBrandByBid(bid);
        return ResponseEntity.ok(brand);
    }

    /**
     * 根据bids批量查询品牌信息
     * @param bids
     * @return
     */
    @GetMapping("brands")
    public ResponseEntity<List<Brand>> findBrandListByBids(@RequestParam List<Long> bids){
        List<Brand> brandList = brandService.findBrandListByBids(bids);
        return ResponseEntity.ok(brandList);
    }

}
