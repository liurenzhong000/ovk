package com.leyou.item.mapper;

import com.leyou.item.pojo.SpecParam;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface SpecparamMapper extends Mapper<SpecParam>, IdListMapper<SpecParam,Long> {
}
