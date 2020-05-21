package com.leyou.item.controller;

import com.leyou.common.vo.PageVO;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

@RestController
@RequestMapping("spu")
public class SpuController {

    @Autowired
    private SpuService spuService;

    /**
     * 分页查询商品spu信息
     * @param page
     * @param rows
     * @param key
     * @param saleable
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageVO> findSpuByPage(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                                @RequestParam(value = "rows",defaultValue = "10")Integer rows,
                                                @RequestParam(value = "key",required = false)String key,
                                                @RequestParam(value = "saleable",required = false)Boolean saleable){
        PageVO<Spu> spuPageVO = spuService.findSpuByPage(page,rows,key,saleable);
        return ResponseEntity.ok(spuPageVO);
    }

    /**
     * 根据spuId查询spuDetail
     * @param spuId
     * @return
     */
    @GetMapping("{spuId}")
    public ResponseEntity<SpuDetail> findSpuDatailBySpuId(@PathVariable("spuId")Long spuId){
        SpuDetail spuDetail = spuService.findSpuDatailBySpuId(spuId);
        return ResponseEntity.ok(spuDetail);
    }

    /**
     * 根据id查询spu信息
     * @param spuId
     * @return
     */
    @GetMapping("/s/{spuId}")
    public ResponseEntity<Spu> findSpuById(@PathVariable("spuId")Long spuId){
        Spu spu = spuService.findSpuById(spuId);
        return ResponseEntity.ok(spu);
    }
}
