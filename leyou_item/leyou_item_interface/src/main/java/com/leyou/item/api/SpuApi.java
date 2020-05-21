package com.leyou.item.api;

import com.leyou.common.vo.PageVO;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("spu")
public interface SpuApi {
    /**
     * 根据spuId查询spuDetail
     * @param spuId
     * @return
     */
    @GetMapping("{spuId}")
    SpuDetail findSpuDatailBySpuId(@PathVariable("spuId")Long spuId);

    @GetMapping("page")
    PageVO findSpuByPage(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                                @RequestParam(value = "rows",defaultValue = "10")Integer rows,
                                                @RequestParam(value = "key",required = false)String key,
                                                @RequestParam(value = "saleable",required = false)Boolean saleable);

    /**
     * 根据id查询spu信息
     * @param spuId
     * @return
     */
    @GetMapping("/s/{spuId}")
    Spu findSpuById(@PathVariable("spuId")Long spuId);
}
