package com.example.getmail.bean;

import com.example.getmail.entity.PlanData;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;
@Data
public class UpdateDailyPlanRequest {
    //PlanData planData;
    Integer plan_id;
    String username;
    String title;
    String content;
    String position;
    Date starttime;
    Date endtime;
    Date plantime;
}
