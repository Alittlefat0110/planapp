package com.schedule.getmail.bean.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 新增planData request
 */
@Data
public class AddDailyPlanRequest {

    /**
     * 所属用户
     */
    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    /**
     * 发件人
     */
    @ApiModelProperty("发件人")
    private String sender;

    /**
     * 收件人
     */
    @ApiModelProperty("收件人")
    private String receiver;

    /**
     * 主题
     */
    @ApiModelProperty("主题")
    @NotBlank(message = "主题不能为空")
    private String title;

    /**
     * 详情
     */
    @ApiModelProperty("详情")
    private String content;

    /**
     * 会议位置
     */
    @ApiModelProperty("会议位置")
    private String position;

    /**
     * 会议开始时间
     */
    @ApiModelProperty("会议开始时间")
    private String startTime;

    /**
     * 会议结束时间
     */
    @ApiModelProperty("会议结束时间")
    private String endTime;

    /**
     * 会议结束时间
     */
    @ApiModelProperty("id,新增时传空")
    Long planId;
}
