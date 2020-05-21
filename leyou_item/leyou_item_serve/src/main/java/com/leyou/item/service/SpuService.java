package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.vo.PageVO;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.mapper.CateGoryMapper;
import com.leyou.item.mapper.SpuDatailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CateGoryService cateGoryService;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CateGoryMapper cateGoryMapper;

    @Autowired
    private SpuDatailMapper spuDatailMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;


    /**
     * 分页查询商品SPU信息
     * @param page
     * @param rows
     * @param key
     * @param saleable
     * @return
     */
    public PageVO<Spu> findSpuByPage(Integer page, Integer rows, String key, Boolean saleable) {
        //分页
        PageHelper.startPage(page,rows);
        //构造查询条件
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }
        if(saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }
        //查询SPU信息
        List<Spu> spuList = spuMapper.selectByExample(example);
        //查询分类名称与品牌名称
       loadBrandNameAndCategoryName(spuList);
        //解析查询结果
        PageInfo<Spu> pageInfo = new PageInfo<>(spuList);
        return new PageVO<Spu>(pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

    /**
     * 获取SPU的分类与品牌名称
     * @param spuList
     */
    private void loadBrandNameAndCategoryName(List<Spu> spuList) {
        spuList.forEach(spu -> {
            //查询品牌名称
            spu.setBname(brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());
            //查询分类名称
            List<String> cnameList = cateGoryMapper.selectByIdList(Arrays.asList(spu.getCid1(), spu.getCid1(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());
            String cname = StringUtils.join(cnameList,"/");
            spu.setCname(cname);
        });
    }

    /**
     * 根据spuId查询spuDetail
     * @param spuId
     * @return
     */
    public SpuDetail findSpuDatailBySpuId(Long spuId) {
        SpuDetail spuDetail = new SpuDetail();
        spuDetail.setSpuId(spuId);
        return spuDatailMapper.selectOne(spuDetail);
    }

    /**
     * 根据id查询spu信息
     * @param spuId
     * @return
     */
    public Spu findSpuById(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        return spu;
    }


    /**
     * 新增商品（false）
     * @param spu
     */
    public void addSpu(Spu spu){
        sendMsg(spu.getId(),"item.insert");
    }

    /**
     * 商品增删改操作向rabbitmq交换机发送消息
     * @param spuId
     * @param routingkey
     */
    public void sendMsg(Long spuId,String routingkey){
        amqpTemplate.convertAndSend("ly.item.exchange",routingkey,spuId);
    }
}
