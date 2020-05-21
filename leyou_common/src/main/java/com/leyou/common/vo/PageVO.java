package com.leyou.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVO<T> {

    //总记录条数
    private Long total;
    //总页数
    private Integer totalPage;
    //当前页数据
    private List<T> items;


}
