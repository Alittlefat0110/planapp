package com.schedule.getmail.constant;


public enum ErrorCode {
    //枚举类限制了此类的实例对象
    SUCCESS(0, "操作成功"),
    DB_ERROR(101, "数据库操作异常");


    //枚举类可以有自己的成员变量和方法
    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
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
