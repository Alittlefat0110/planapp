package com.schedule.getmail.bean.request;

import lombok.Data;

/**
 * 根据收件人查询
 */
@Data
public class SelectConferenceDataByToEmailRequest {
    private String email;
}
