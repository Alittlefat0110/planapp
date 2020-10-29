package com.schedule.getmail.bean.request;

import lombok.Data;

/**
 * 时间轴按天查询条件
 */
@Data
public class SelectPlanDataByTimeRequest {

    private String userName;

    private String time;
}
