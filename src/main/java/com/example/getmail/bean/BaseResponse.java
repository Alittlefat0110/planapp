package com.example.getmail.bean;

import com.example.getmail.constant.ErrorCode;

public class BaseResponse {

    private int code=0;

    private String message;

    public void setErrorCode(ErrorCode errorCode){
        this.code=errorCode.getCode();
        this.message=errorCode.getMessage();
    }
}
