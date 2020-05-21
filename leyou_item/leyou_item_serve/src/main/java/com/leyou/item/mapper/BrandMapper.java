package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand>, IdListMapper<Brand,Long> {
    @Insert("insert into tb_category_brand(category_id,brand_id) values(#{cid},#{brandId})")
    int insertBrandIdAndCateGoryId(@Param("brandId") Long brandId, @Param("cid") Long cid);

    @Select("select b.* from tb_brand b join tb_category_brand cb on b.id = cb.brand_id where cb.category_id = #{cid}")
    List<Brand> selectBrandListByCid(@Param("cid") Long cid);
}
