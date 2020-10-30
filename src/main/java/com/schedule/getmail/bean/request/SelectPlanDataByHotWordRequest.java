package com.schedule.getmail.bean.request;

import lombok.Data;

/**
 * 时间轴按天查询条件
 */
@Data
public class SelectPlanDataByHotWordRequest {
    private String userName;
    private String hotWords;
}
