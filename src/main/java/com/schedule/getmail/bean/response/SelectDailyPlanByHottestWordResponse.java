package com.schedule.getmail.bean.response;

import com.schedule.getmail.entity.PlanData;
import lombok.Data;
import java.util.List;

@Data
public class SelectDailyPlanByHottestWordResponse extends BaseResponse{
    private List<PlanData> data;
}
