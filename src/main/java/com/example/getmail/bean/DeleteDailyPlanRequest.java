package com.example.getmail.bean;

import com.example.getmail.entity.PlanData;
import lombok.Data;

import java.util.List;

@Data
public class DeleteDailyPlanRequest {
     String username;
     List<Integer> plan_id;
}
