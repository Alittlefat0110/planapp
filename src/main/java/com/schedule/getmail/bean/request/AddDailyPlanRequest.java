package com.schedule.getmail.bean.request;

import lombok.Data;

import java.util.Date;

/**
 * 新增planData request
 */
@Data
public class AddDailyPlanRequest {
    String userName;
    String title;
    String content;
    String position;
    Date startTime;
    Date endTime;
    Date planTime;
    Long planId;
}
