package com.schedule.getmail.mapper;

import com.schedule.getmail.entity.PlanData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value="dailyPlanAddBySelfMapper")
public interface DailyPlanAddBySelfMapper {
    int dailyPlanAddBySelf(@Param("list") List<PlanData> list); //手动新增日程表

    int dailyPlanDeleteById(@Param("username") String username, @Param("plan_id") List<Integer> plan_id);//删除日程表

    int dailyPlanUpdateById(PlanData planData);//更新日程
}
