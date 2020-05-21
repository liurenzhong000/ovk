package com.leyou.common.exception;

import com.leyou.common.enums.ExceptionEnums;
import lombok.Getter;

import java.util.UUID;

@Getter
//@AllArgsConstructor
//@NoArgsConstructor
public class LyException extends RuntimeException {

    private ExceptionEnums exceptionEnums;

    public LyException(ExceptionEnums exceptionEnums) {
        this.exceptionEnums = exceptionEnums;
    }

    public LyException() {
    }


}
