package com.leyou.search.pojo;

import com.leyou.common.vo.PageVO;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SearchPageVO<T> extends PageVO<T> {

    private List<Category> categoryies;//分类待选项

    private List<Brand> brands;//品牌待选项

    private List<Map<String,Object>> specs; //规格参数待选项

    public SearchPageVO() {
    }

    public SearchPageVO(Long total, Integer totalPage, List<T> items, List<Category> categoryies, List<Brand> brands, List<Map<String,Object>> specs) {
        super(total, totalPage, items);
        this.categoryies = categoryies;
        this.brands = brands;
        this.specs = specs;
    }
}
