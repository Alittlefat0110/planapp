package com.schedule.getmail.bean.request;

import lombok.Data;

import java.util.List;

@Data
public class DeleteDailyPlanRequest {
     String username;
     List<Integer> plan_id;
}
