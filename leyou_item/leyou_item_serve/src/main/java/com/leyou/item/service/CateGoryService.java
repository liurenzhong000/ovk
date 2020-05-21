package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.CateGoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CateGoryService {

    @Autowired
    private CateGoryMapper cateGoryMapper;


    /**
     * 根据父节点查询商品列表
     * @param pid
     * @return
     */
    public List<Category> findCategoryListByPid(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        List<Category> categoryList = cateGoryMapper.select(category);
        if(CollectionUtils.isEmpty(categoryList)){
            throw new LyException(ExceptionEnums.CATEGORY_CAT_FOUND);
        }
        return categoryList;
    }


    /**
     * 根据ids批量查询商品类目
     * @param ids
     * @return
     */
    public List<Category> findCategoryListByIds(List<Long> ids) {
        return cateGoryMapper.selectByIdList(ids);
    }
}
