package com.schedule.getmail.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.schedule.getmail.bean.request.*;
import com.schedule.getmail.bean.response.*;
import com.schedule.getmail.constant.ErrorCode;
import com.schedule.getmail.entity.PlanData;
import com.schedule.getmail.service.IPlanDataService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 日程数据表 前端控制器
 * </p>
 * @author StrTom
 * @since 2020-10-28
 */
@Slf4j(topic = "planData")
@RestController
public class PlanDataController {

    @Resource
    private IPlanDataService planDataService;


    /**
     * 时间轴查询
     * @param request
     * @return
     */
    @ApiOperation(value = "时间轴接口", notes="时间轴接口")
    @PostMapping(value = "/dailyPlan/listByTimeRange", produces = "application/json;charset=utf-8")
    public SelectPlanDataResponse selectByTimeRange (@RequestBody SelectDailyPlanByTimeRangeRequest request) {
        SelectPlanDataResponse response=new SelectPlanDataResponse();
        try {
            List<PlanData> list=planDataService.selectByTimeRange(request.getUserName(),request.getPageIndex());
            response.setData(list);
            response.setErrorCode(ErrorCode.SUCCESS);
            return response;
        }catch(Exception e){
            log.error("",e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }

    /**
     * 时间轴按天查询
     * @param request
     * @return
     */
    @ApiOperation(value = "时间轴按天查询接口", notes="时间轴按天查询接口")
    @PostMapping(value = "/dailyPlan/getPlanDataByTime", produces = "application/json;charset=utf-8")
    public SelectPlanDataResponse selectPlanDataByTime (@RequestBody SelectPlanDataByTimeRequest request) {
        SelectPlanDataResponse response=new SelectPlanDataResponse();
        try {
            List<PlanData> list = planDataService.list(new QueryWrapper<PlanData>().lambda()
                    .eq(!StringUtils.isEmpty(request.getUserName()), PlanData::getUsername, request.getUserName())
                    .eq(!StringUtils.isEmpty(request.getTime()), PlanData::getStartTime, request.getTime())
                    .orderByDesc(PlanData::getStartTime));
            response.setData(list);
            response.setErrorCode(ErrorCode.SUCCESS);
            return response;
        }catch(Exception e){
            log.error("",e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }

    /**
     * 日历程查询接口
     * @param request
     * @return
     */
    @ApiOperation(value = "日历程接口", notes="日历程接口")
    @PostMapping(value = "/dailyPlan/listByMonthRange", produces = "application/json;charset=utf-8")
    public SelectPlanDataResponse selectByMonthRange (@RequestBody SelectDailyPlanByMonthRangeRequest request) {
        SelectPlanDataResponse response=new SelectPlanDataResponse();
        try {
            List<PlanData> list=planDataService.selectByMonthRange(request.getUserName());
            response.setData(list);
            response.setErrorCode(ErrorCode.SUCCESS);
            return response;
        }catch(Exception e){
            log.error("",e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }

//    /**
//     * 时间轴按天查询
//     * @param request
//     * @return
//     */
//    @ApiOperation(value = "日历程按天查询接口", notes="日历程按天查询接口")
//    @PostMapping(value = "/dailyPlan/getPlanDataByMonth", produces = "application/json;charset=utf-8")
//    public SelectPlanDataResponse selectPlanDataByMonth (@RequestBody SelectPlanDataByTimeRequest request) {
//        SelectPlanDataResponse response=new SelectPlanDataResponse();
//        try {
//            List<PlanData> list = planDataService.list(new QueryWrapper<PlanData>().lambda()
//                    .eq(!StringUtils.isEmpty(request.getUserName()), PlanData::getUsername, request.getUserName())
//                    .eq(!StringUtils.isEmpty(request.getTime()), PlanData::getStarttime, request.getTime())
//                    .ne(PlanData::getSource,"Message")
//                    .orderByDesc(PlanData::getStarttime));
//            response.setData(list);
//            response.setErrorCode(ErrorCode.SUCCESS);
//            return response;
//        }catch(Exception e){
//            log.error("",e);
//            response.setErrorCode(ErrorCode.DB_ERROR);
//        }
//        return response;
//    }


    /**
     * 手动添加、修改日程
     * @param request
     * @return
     */
    @ApiOperation(value = "手动添加、修改日程接口", notes="手动添加、修改日程接口")
    @PostMapping(value = "/dailyPlan/saveOrUpdate", produces = "application/json;charset=utf-8")
    public AddDailyPlanResponse saveOrUpdate(@RequestBody AddDailyPlanRequest request){
        AddDailyPlanResponse response=new AddDailyPlanResponse();
        boolean b = planDataService.saveOrUpdate(request);
        if(b){
            response.setErrorCode(ErrorCode.SUCCESS);
        }else {
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }

    /**
     * 删除日程
     * @param request
     * @return
     */
    @ApiOperation(value = "删除日程接口", notes="删除日程接口")
    @PostMapping(value = "/dailyPlan/delete", produces = "application/json;charset=utf-8")
    public DeleteDailyPlanReponse dailyPlanDeleteById(@RequestBody DeleteDailyPlanRequest request ){
        DeleteDailyPlanReponse response=new  DeleteDailyPlanReponse();
        try {
            planDataService.remove(new QueryWrapper<PlanData>().lambda()
                    .eq(!StringUtils.isEmpty(request.getUserName()), PlanData::getUsername, request.getUserName())
                    .eq(!StringUtils.isEmpty(request.getPlanId()), PlanData::getPlanId, request.getPlanId())
                    .orderByDesc(PlanData::getStartTime));
            response.setErrorCode(ErrorCode.SUCCESS);
        }catch(Exception e){
            log.error("",e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }
}
