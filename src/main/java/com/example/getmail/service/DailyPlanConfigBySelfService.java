package com.example.getmail.service;

import com.example.getmail.entity.PlanData;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

public interface DailyPlanConfigBySelfService {
    int dailyPlanAddBySelf(List<PlanData> list); //手动新增日程表
    int dailyPlanDeleteById(String username,List<Integer> plan_id);//删除日程表、
    int dailyPlanUpdateById(PlanData planData);//更新日程
}
