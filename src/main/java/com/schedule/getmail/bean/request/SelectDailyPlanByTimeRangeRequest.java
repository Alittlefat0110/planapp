package com.schedule.getmail.bean.request;

import lombok.Data;

/**
 * 时间轴 查询 request
 */
@Data
public class SelectDailyPlanByTimeRangeRequest {

    String userName;

    int pageIndex;
}
