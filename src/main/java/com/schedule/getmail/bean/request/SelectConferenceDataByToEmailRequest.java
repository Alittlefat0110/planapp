package com.schedule.getmail.bean.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 根据收件人查询
 */
@Data
public class SelectConferenceDataByToEmailRequest {
    /**
     * email
     */
    @ApiModelProperty("收件人邮箱")
    @NotBlank(message = "收件人邮箱")
    private String email;
}
