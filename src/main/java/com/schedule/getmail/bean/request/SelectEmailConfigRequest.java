package com.schedule.getmail.bean.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 查询绑定邮箱,时间设置,关键词、发件人过滤接口 request
 */
@Data
public class SelectEmailConfigRequest {

    /**
     * 所属用户
     */
    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    String userName;
}
