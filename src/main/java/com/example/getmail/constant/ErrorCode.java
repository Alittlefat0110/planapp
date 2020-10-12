package com.example.getmail.constant;

import lombok.Data;


public enum ErrorCode {
    SUCCESS(0,"操作成功"),
    DB_ERROR(101,"数据库操作异常");

    private int code;

    private String message;

     ErrorCode(int code,String message){
        this.code=code;
        this.message=message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
