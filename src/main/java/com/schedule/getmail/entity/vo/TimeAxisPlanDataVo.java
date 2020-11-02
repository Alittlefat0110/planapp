package com.schedule.getmail.entity.vo;


import com.schedule.getmail.entity.PlanData;
import lombok.Data;

import java.util.List;

/**
 * 时间轴返回
 */
@Data
public class TimeAxisPlanDataVo {
    private String time;
    private List<PlanData> data;
}
