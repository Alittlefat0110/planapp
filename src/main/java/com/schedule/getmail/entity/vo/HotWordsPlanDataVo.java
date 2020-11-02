package com.schedule.getmail.entity.vo;

import com.schedule.getmail.entity.PlanData;
import lombok.Data;

import java.util.List;

/**
 * 热词统计及数据
 */
@Data
public class HotWordsPlanDataVo {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 热词出现次数
     */
    private int sum;

    /**
     * 热词出现的list
     */
    private List<PlanData> data;
}
