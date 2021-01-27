package com.schedule.getmail.bean.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.sql.Timestamp;

/**
 * insert 绑定邮箱,时间设置,关键词、发件人过滤  request
 */
@Data
public class AddEmailConfigRequest {

    /**
     * 所属用户
     */
    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    /**
     * 邮箱地址
     */
    @ApiModelProperty("邮箱地址")
    @NotBlank(message = "邮箱地址不能为空")
    private String email;

    /**
     * 邮箱登陆密码
     */
    @ApiModelProperty("邮箱密码")
    @NotBlank(message = "邮箱密码不能为空")
    private String password;

    /**
     * 部门
     */
    @ApiModelProperty("部门")
    //@NotBlank(message = "部门不能为空")
    private String department;

    /**
     * 岗位
     */
    @ApiModelProperty("岗位")
    //@NotBlank(message = "岗位不能为空")
    private String station;

    /**
     * 设置邮箱同步开始时间
     */
    @ApiModelProperty("设置邮箱同步开始时间")
    private String startTime;

    /**
     * 过滤关键词/角色标签
     */
    @ApiModelProperty("过滤关键词/角色标签")
    private String keyWordS;

    /**
     * 过滤关键词/通用标签
     */
    @ApiModelProperty("过滤关键词/通用标签")
    private String keyWordT;

    /**
     * 过滤邮箱
     */
    @ApiModelProperty("过滤邮箱")
    private String keyEmail;
    /**
     * 是否只接收chinamoney邮件  1：是/ 0：不是
     */
    @ApiModelProperty("是否只接收chinamoney邮件  1：是/ 0：不是")
    private String flag;

}
