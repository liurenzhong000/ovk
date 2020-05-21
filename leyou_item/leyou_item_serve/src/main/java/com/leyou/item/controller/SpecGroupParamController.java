package com.leyou.item.controller;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.pojo.Spu;
import com.leyou.item.service.SpecGroupParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecGroupParamController {

    @Autowired
    private SpecGroupParamService specGroupParamService;

    /**
     * 查询指定分类的规格组与规格参数
     * @param cid
     * @return
     */
    @GetMapping("group/{cid}")
    public ResponseEntity<List<SpecGroup>> findSpecGroups(@PathVariable("cid")Long cid){
        List<SpecGroup> specGroupList = specGroupParamService.findSpecGroups(cid);
        if(CollectionUtils.isEmpty(specGroupList)){
            throw new LyException(ExceptionEnums.SPEC_GROUP_NOT_FOUND);
        }
        return ResponseEntity.ok(specGroupList);
    }

    /**
     * 查询规格组参数
     * @param gid
     * @return
     */
    @GetMapping("param/{gid}")
    public ResponseEntity<List<String>> findSpecParams(@PathVariable("gid")Long gid){

        List<String> specParamList = specGroupParamService.findSpecParams(gid);
        if(CollectionUtils.isEmpty(specParamList)){
            throw new LyException(ExceptionEnums.SPEC_GROUP_PARAM_NOT_FOUND);
        }
        return ResponseEntity.ok(specParamList);
    }

    /**
     * 根据分类ID查询规格参数
     * @param cid
     * @return
     */
    @GetMapping("{cid}")
    public ResponseEntity<List<SpecParam>> querySpecParamListByCid(@PathVariable("cid")Long cid,
                                                                   @RequestParam(value = "search",required = false)Integer search){
        List<SpecParam> specParamList = specGroupParamService.querySpecParamListByCid(cid,search);
        return ResponseEntity.ok(specParamList);
    }
}
