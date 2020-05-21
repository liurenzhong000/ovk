package com.leyou.item.mapper;

import com.leyou.item.pojo.Sku;
import org.omg.PortableInterceptor.LOCATION_FORWARD;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface SkuMapper extends Mapper<Sku>,IdListMapper<Sku,Long> {
}
