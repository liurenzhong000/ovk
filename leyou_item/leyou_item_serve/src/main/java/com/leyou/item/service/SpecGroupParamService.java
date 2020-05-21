package com.leyou.item.service;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecparamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SpecGroupParamService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecparamMapper specparamMapper;

    /**
     * 查询指定分类的规格组与规格参数
     * @param cid 分类ID
     * @return
     */
    public List<SpecGroup> findSpecGroups(Long cid) {
        //获取specgroup信息
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> specGroupList = specGroupMapper.select(specGroup);
        //获取specparam信息
        List<SpecParam> specParamList = specparamMapper.selectByIdList(specGroupList.stream().map(SpecGroup::getId).collect(Collectors.toList()));
        Map<Long,List<SpecParam>> specParamMap = new HashMap<>();
        specParamList.forEach(sg -> {
            if(!specParamMap.containsKey(sg.getGroupId())){
                specParamMap.put(sg.getGroupId(),new ArrayList<>());
            }
            specParamMap.get(sg.getGroupId()).add(sg);
        });
        specGroupList.forEach(g -> {
            g.setParams(specParamMap.get(g.getId()));
        });

        return specGroupList;
    }

    /**
     * 查询规格组参数
     * @param gid 规格组ID
     * @return
     */
    public List<String> findSpecParams(Long gid) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        List<SpecParam> specParamList = specparamMapper.select(specParam);
        List<String> paramNameList = specParamList.stream().map(sp -> sp.getName()).collect(Collectors.toList());
        return paramNameList;
    }

    /**
     * 根据分类ID查询规格参数
     * @param cid 分类ID
     * @return
     */
    public List<SpecParam> querySpecParamListByCid(Long cid,Integer search) {
        SpecParam specParam = new SpecParam();
        specParam.setCid(cid);
        specParam.setSearching(search);
        List<SpecParam> specParamList = specparamMapper.select(specParam);
        return specParamList;
    }
}
