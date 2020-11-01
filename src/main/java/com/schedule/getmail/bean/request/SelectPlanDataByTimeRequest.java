package com.schedule.getmail.bean.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 时间轴按天查询条件
 */
@Data
public class SelectPlanDataByTimeRequest {
    /**
     * 所属用户
     */
    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    /**
     * 日期
     */
    @ApiModelProperty("日期")
    @NotBlank(message = "日期不能为空")
    private String time;
}
