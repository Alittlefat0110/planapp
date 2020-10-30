package com.schedule.getmail.bean.request;


import lombok.Data;

import java.sql.Timestamp;

/**
 * insert 绑定邮箱,时间设置,关键词、发件人过滤  request
 */
@Data
public class AddEmailConfigRequest {

    /**
     * 所属用户
     */
    private String userName;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 邮箱登陆密码
     */
    private String password;

    /**
     * 部门
     */
    private String department;

    /**
     * 岗位
     */
    private String station;

    /**
     * 设置邮箱同步开始时间
     */
    private String startTime;

    /**
     * 过滤关键词/角色标签
     */
    private String keyWordS;

    /**
     * 过滤关键词/通用标签
     */
    private String keyWordT;

    /**
     * 过滤邮箱
     */
    private String keyEmail;
    /**
     * 是否只接收chinamoney邮件  1：是/ 0：不是
     */
    private String flag;

}
