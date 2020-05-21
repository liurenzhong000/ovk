package com.leyou.user.pojo;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @Author: 98050
 * @Time: 2018-10-21 18:42
 * @Feature: 用户实体类
 */
@Data
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Length(min = 4,max = 15,message = "用户名只能在4~15位之间")
    private String username;

    /**
     * 密码
     */
    //@JsonIgnore
    @Length(min = 6,max = 25,message = "密码只能在6~25位之间")
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 电话
     */
    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = "^1[35678]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 创建时间
     */
    private Date created;


}
