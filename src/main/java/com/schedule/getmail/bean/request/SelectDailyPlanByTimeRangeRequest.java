package com.schedule.getmail.bean.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 时间轴 查询 request
 */
@Data
public class SelectDailyPlanByTimeRangeRequest {

    /**
     * 所属用户
     */
    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    /**
     * 当前时间对应的当前周（pageIndex=0）及偏移（pageIndex=-1：上一周 / pageIndex=1：下一周）
     */
    @ApiModelProperty("前时间对应的当前周（pageIndex=0）及偏移（pageIndex=-1：上一周 / pageIndex=1：下一周）")
    @NotBlank(message = "周不能为空")
    private int pageIndex;
}
