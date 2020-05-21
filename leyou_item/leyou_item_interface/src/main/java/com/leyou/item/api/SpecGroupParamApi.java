package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("spec")
public interface SpecGroupParamApi {
    @GetMapping("{cid}")
    List<SpecParam> querySpecParamListByCid(@PathVariable("cid")Long cid,
                                            @RequestParam(value = "search",required = false)Integer search);

    /**
     * 查询指定分类的规格组与规格参数
     * @param cid
     * @return
     */
    @GetMapping("group/{cid}")
    List<SpecGroup> findSpecGroups(@PathVariable("cid")Long cid);

}
