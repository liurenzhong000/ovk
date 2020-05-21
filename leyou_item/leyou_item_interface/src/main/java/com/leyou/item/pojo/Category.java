package com.leyou.item.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "tb_category")
@Data
public class Category implements Serializable {

    @Id
    @KeySql(useGeneratedKeys = true)
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //分类ID
    private Long id;
    //分类名称
    private String name;
    //父节点ID
    private Long parentId;
    //是否为父节点（0否 1是）
    private Integer isParent;
    //排序指数，越小越靠前
    private Integer sort;

}
