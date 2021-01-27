package com.schedule.getmail.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedule.getmail.BootstrapperTest;
import com.schedule.getmail.bean.request.*;
import com.schedule.getmail.bean.response.*;
import com.schedule.getmail.constant.ErrorCode;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


public class PlanDataControllerTest extends BootstrapperTest {

    @Autowired
    ObjectMapper objectMapper;

    private static final String selectByTimeRange = "/dailyPlan/listByTimeRange";
    private static final String selectPlanDataByTime = "/dailyPlan/getPlanDataByTime";
    private static final String selectByMonthRange = "/dailyPlan/listByMonthRange";
    private static final String saveOrUpdate = "/dailyPlan/saveOrUpdate";
    private static final String dailyPlanDeleteById = "/dailyPlan/delete";
    private static final String selectByHotWords = "/dailyPlan/getByHotWords";

    @Value("classpath:data/querySelectByTimeRange.json")
    private Resource querySelectByTimeRange;
    @Value("classpath:data/querySelectPlanDataByTime.json")
    private Resource querySelectPlanDataByTime;
    @Value("classpath:data/querySelectByMonthRange.json")
    private Resource querySelectByMonthRange;
    @Value("classpath:data/queryPlanDataSaveOrUpdate.json")
    private Resource queryPlanDataSaveOrUpdate;
    @Value("classpath:data/queryDailyPlanDeleteById.json")
    private Resource queryDailyPlanDeleteById;
    @Value("classpath:data/querySelectByHotWords.json")
    private Resource querySelectByHotWords;

    @Test
    public void selectByTimeRange() throws Exception{
        SelectDailyPlanByTimeRangeRequest request = objectMapper.readValue(querySelectByTimeRange.getInputStream(),
                SelectDailyPlanByTimeRangeRequest.class);
        SelectTimeAxisPlanDataResponse response = restPost(selectByTimeRange,request,SelectTimeAxisPlanDataResponse.class);
        assertThat(response.getCode(),equalTo(ErrorCode.SUCCESS.getCode()));
    }

    @Test
    public void selectPlanDataByTime() throws Exception{
        SelectPlanDataByTimeRequest request = objectMapper.readValue(querySelectPlanDataByTime.getInputStream(),
                SelectPlanDataByTimeRequest.class);
        SelectPlanDataResponse response = restPost(selectPlanDataByTime,request,SelectPlanDataResponse.class);
        assertThat(response.getCode(),equalTo(ErrorCode.SUCCESS.getCode()));
    }

    @Test
    public void selectByMonthRange() throws Exception{
        SelectDailyPlanByMonthRangeRequest request = objectMapper.readValue(querySelectByMonthRange.getInputStream(),
                SelectDailyPlanByMonthRangeRequest.class);
        SelectPlanDataResponse response = restPost(selectByMonthRange,request,SelectPlanDataResponse.class);
        assertThat(response.getCode(),equalTo(ErrorCode.SUCCESS.getCode()));
    }

    @Test
    public void saveOrUpdate() throws Exception{
        AddDailyPlanRequest request = objectMapper.readValue(queryPlanDataSaveOrUpdate.getInputStream(),
                AddDailyPlanRequest.class);
        AddDailyPlanResponse response = restPost(saveOrUpdate,request,AddDailyPlanResponse.class);
        assertThat(response.getCode(),equalTo(ErrorCode.SUCCESS.getCode()));
    }

    @Test
    public void dailyPlanDeleteById() throws Exception{
        DeleteDailyPlanRequest request = objectMapper.readValue(queryDailyPlanDeleteById.getInputStream(),
                DeleteDailyPlanRequest.class);
        DeleteDailyPlanReponse response = restPost(dailyPlanDeleteById,request,DeleteDailyPlanReponse.class);
        assertThat(response.getCode(),equalTo(ErrorCode.SUCCESS.getCode()));
    }

    @Test
    public void selectByHotWords() throws Exception{
        SelectPlanDataByHotWordRequest request = objectMapper.readValue(querySelectByHotWords.getInputStream(),
                SelectPlanDataByHotWordRequest.class);
        SelectPlanDataByHotWordsResponse response = restPost(selectByHotWords,request,SelectPlanDataByHotWordsResponse.class);
        assertThat(response.getCode(),equalTo(ErrorCode.SUCCESS.getCode()));
    }


}
