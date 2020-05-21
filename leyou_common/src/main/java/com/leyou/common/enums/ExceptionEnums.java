package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ExceptionEnums {

    BRAND_NOT_FOUND(404,"品牌不存在"),
    BRAND_NAME_CANT_BE_NULL(400,"品牌名称不能为空"),
    BRAND_IMAGE_CANT_BE_NULL(400,"品牌图片不能为空"),
    BRAND_LETTER_CANT_BE_NULL(400,"品牌首字母不能为空"),
    BRAND_BLONG_CATEGORY_ID(400,"品牌所属分类ID不能为空"),
    BRAND_LETTER_ILLEGAL(400,"品牌首字母非法"),
    BRAND_ADD_FAIL(500,"新增品牌失败"),
    CATEGORY_CAT_FOUND(404,"商品分类不存在"),
    BRAND_ALREADY_EXIST(400,"品牌名称已经存在"),
    FILE_TYPE_ERROR(400,"文件类型不支持"),
    FILE_CONTENT_ERROR(400,"文件内容不符合要求"),
    FILE_UPLOAD_FAIL(500,"文件上传失败"),
    SPEC_GROUP_NOT_FOUND(404,"规格组不存在"),
    SPEC_GROUP_PARAM_NOT_FOUND(404,"规格组参数不存在"),
    TYPE_PARAMETER_ERROR(400,"类型参数有误"),
    USER_NAME_ALREADY_EXISTS_ERROR(400,"用户名已经存在"),
    PHONE_ALREADY_REGISTER_ERROR(400,"手机号已经注册过"),
    PHONE_CODE_ERROR(400,"验证码错误"),
    USER_NAME_ORPWD_ERROR(400,"用户名或密码错误"),
    SERVER_BUSY_ERROR(500,"服务端繁忙，请稍后重试"),
    NOT_LOGIN(403,"未登录"),
    CART_NOT_FOUNT(404,"购物车为空"),
;

    private int code;
    private String msg;


}
