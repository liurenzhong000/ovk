package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageVO;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.mapper.CateGoryMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CateGoryMapper cateGoryMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 分页查询品牌信息
     * @param curPage
     * @param pageSize
     * @param key
     * @param orderBy
     * @param desc
     * @return
     */
    public PageVO<Brand> findBrandListByPage(Integer curPage, Integer pageSize, String key, String orderBy, Boolean desc) {
        //使用分页助手进行分页
        PageHelper.startPage(curPage,pageSize);
        //过滤条件
        Example example =new Example(Brand.class);
        if(StringUtils.isNotBlank(key)){
            example.createCriteria().orLike("name","%"+key+"%").orEqualTo("letter",key.toUpperCase());
        }
       example.setOrderByClause(orderBy + (desc ? " desc" : " asc"));
        //查询
        List<Brand> brandList = brandMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(brandList)){
            throw new LyException(ExceptionEnums.CATEGORY_CAT_FOUND);
        }
        //解析分页结果
        PageInfo<Brand> pageInfo = new PageInfo<>(brandList);
        return new PageVO<>(pageInfo.getTotal(),pageInfo.getPages(),brandList);
    }

    /**
     * 新增品牌
     * @param brand
     * @param cids
     */
    @Transactional
    public void addBrand(Brand brand, List<Long> cids) {

        /**
         * 判断所选品牌分类与品牌名是否存在
         */
        //判断品牌是否存在
        Brand b = new Brand();
        brand.setName(brand.getName());
         if(brandMapper.selectOne(brand) != null){
             throw new LyException(ExceptionEnums.BRAND_ALREADY_EXIST);
         }
        //判断所选品牌分类是否存在
        cids.forEach(cid -> {
            Category category = cateGoryMapper.selectByPrimaryKey(cid);
            if(category == null){
                throw new LyException(ExceptionEnums.CATEGORY_CAT_FOUND);
            }
        });

        /**
         * 新增品牌
         */
        brand.setId(null);
        int insertBrandCount = brandMapper.insert(brand);
        //新增品牌失败
        if(! (insertBrandCount == 1)){
            log.error("新增品牌失败");
            throw new LyException(ExceptionEnums.BRAND_ADD_FAIL);
        }
        cids.forEach(cid -> {
            //新增品牌分类中间表ID
            int insertBrandIdAndCateGoryIdCount = brandMapper.insertBrandIdAndCateGoryId(brand.getId(),cid);
            //新增品牌分类中间表ID失败
            if(! (insertBrandIdAndCateGoryIdCount == 1)){
                throw new LyException(ExceptionEnums.BRAND_ADD_FAIL);
            }});
    }

    /**
     * 根据分类ID查询品牌列表
     * @param cid
     * @return
     */
    public List<Brand> queryBrandListByCid(Long cid) {
        List<Brand> brandList = brandMapper.selectBrandListByCid(cid);
        return brandList;
    }

    /**
     * 根据品牌ID查询品牌信息
     * @param bid
     * @return
     */
    public Brand findBrandByBid(Long bid) {
        Brand brand = brandMapper.selectByPrimaryKey(bid);
        return brand;
    }

    /**
     * 根据bids批量查询品牌信息
     * @param bids
     * @return
     */
    public List<Brand> findBrandListByBids(List<Long> bids) {
        return brandMapper.selectByIdList(bids);
    }
}
