package com.schedule.getmail.bean.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 时间轴按天查询条件
 */
@Data
public class SelectPlanDataByHotWordRequest {
    /**
     * 所属用户
     */
    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    /**
     * 热词
     */
    @ApiModelProperty("热词")
    @NotBlank(message = "热词不能为空")
    private String hotWords;
}
