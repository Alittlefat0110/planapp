package com.schedule.getmail.bean.response;

import com.schedule.getmail.entity.PlanData;
import lombok.Data;

import java.util.List;

/**
 * PlanData 结果返回
 */
@Data
public class SelectPlanDataResponse extends BaseResponse {
    private List<PlanData> data;
}
