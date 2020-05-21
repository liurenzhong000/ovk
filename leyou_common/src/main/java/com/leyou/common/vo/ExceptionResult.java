package com.leyou.common.vo;


import com.leyou.common.enums.ExceptionEnums;
import lombok.Data;

@Data
public class ExceptionResult {

    private String msg;
    private int code;
    private Long timestamp;

    public ExceptionResult(ExceptionEnums exceptionEnums){
        this.msg = exceptionEnums.getMsg();
        this.code = exceptionEnums.getCode();
        this.timestamp= System.currentTimeMillis();
    }





}
