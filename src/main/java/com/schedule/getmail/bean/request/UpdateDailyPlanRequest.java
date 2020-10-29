package com.schedule.getmail.bean.request;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;
@Data
public class UpdateDailyPlanRequest {
    //PlanData planData;
    Long planId;
    String username;
    String title;
    String content;
    String position;
    Date starttime;
    Date endtime;
    Date plantime;
}
