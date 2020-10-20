package com.example.getmail.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.example.getmail.entity.ConferenceData;
import com.example.getmail.entity.PlanData;
import com.example.getmail.mapper.DailyPlanAddBySelfMapper;
import com.example.getmail.service.DailyPlanConfigBySelfService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service(value = "DailyPlanBySelf")
public class DailyPlanAddBySelfServiceImpl implements DailyPlanConfigBySelfService {
    @Resource
    //@Autowired
    private DailyPlanAddBySelfMapper dailyPlanAddBySelfMapper;

    @Override
    //手动插入日程表
    public int dailyPlanAddBySelf(List<PlanData> list) {

        return dailyPlanAddBySelfMapper.dailyPlanAddBySelf(list);
    }


    @Override
    //删除日程
    public int dailyPlanDeleteById(String username,List<Integer> plan_id){

        return dailyPlanAddBySelfMapper.dailyPlanDeleteById(username,plan_id);
    }
    @Override
    //post 更新日程
    public int dailyPlanUpdateById(PlanData planData){

        return dailyPlanAddBySelfMapper.dailyPlanUpdateById(planData);
    }
}
