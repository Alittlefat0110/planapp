package com.example.getmail.bean;

import com.example.getmail.entity.EmailConfig;
import com.example.getmail.entity.PlanData;
import lombok.Data;

import java.util.List;

@Data
public class SelectDailyPlanByTimeRangeResponse extends BaseResponse{
    private List<PlanData> data;
}
