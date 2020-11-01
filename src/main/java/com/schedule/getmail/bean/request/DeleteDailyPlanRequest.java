package com.schedule.getmail.bean.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * 删除planData request
 */
@Data
public class DeleteDailyPlanRequest {
     /**
      * 所属用户
      */
     @ApiModelProperty("用户名")
     @NotBlank(message = "用户名不能为空")
     String userName;
     /**
      * 所属用户
      */
     @ApiModelProperty("planId")
     @NotBlank(message = "planId不能为空")
     String planId;;
}
