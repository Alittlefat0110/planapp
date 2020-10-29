package com.schedule.getmail.controller;


import com.schedule.getmail.bean.request.AddDailyPlanRequest;
import com.schedule.getmail.bean.request.DeleteDailyPlanRequest;
import com.schedule.getmail.bean.request.SelectDailyPlanByTimeRangeRequest;
import com.schedule.getmail.bean.request.UpdateDailyPlanRequest;
import com.schedule.getmail.bean.response.*;
import com.schedule.getmail.constant.ErrorCode;
import com.schedule.getmail.entity.PlanData;
import com.schedule.getmail.service.IPlanDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
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
     * 同步邮箱表至planData
     * @return
     */
    @PostMapping(value = "/dailyPlan/getFromConference", produces = "application/json;charset=utf-8")
    public AddDailyPlanFromConferenceResponse dailyPlanGetFromConference(){
        AddDailyPlanFromConferenceResponse response=new AddDailyPlanFromConferenceResponse();
//        try {
//            getMailService.dailyPlanGetFromConference();
//            response.setErrorCode(ErrorCode.SUCCESS);
//        }catch(Exception e){
//            log.error("",e);
//            response.setErrorCode(ErrorCode.DB_ERROR);
//        }
        return response;
    }


    /**
     * 从日程表中查询指定时间段（当前周）内的日程信息
     * @param request
     * @return
     */
    @PostMapping(value = "/dailyPlan/listByTimeRange", produces = "application/json;charset=utf-8")
    public SelectDailyPlanByTimeRangeResponse selectByTimeRange (@RequestBody SelectDailyPlanByTimeRangeRequest request) {
        SelectDailyPlanByTimeRangeResponse response=new SelectDailyPlanByTimeRangeResponse();
        try {
            List<PlanData> list=planDataService.selectByTimeRange(request.getUsername(),request.getPageIndex());
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
     * 从日程表中查询指定时间段（按日期）内的日程信息
     * @param request
     * @return
     */
//    @PostMapping(value = "/dailyPlan/listByTimeRange2", produces = "application/json;charset=utf-8")
//    public SelectDailyPlanByTimeRangeResponse selectByTimeRange2 (@RequestBody SelectDailyPlanByTimeRangeRequest request) {
//        SelectDailyPlanByTimeRangeResponse response=new SelectDailyPlanByTimeRangeResponse();
//        try {
//            List<PlanData> list=planDataService.selectByTimeRange(request.getUsername(),request.getPageIndex());
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
     * 手动添加日程
     * @param request
     * @return
     */
    @PostMapping(value = "/dailyPlan/add", produces = "application/json;charset=utf-8")
    public AddDailyPlanResponse dailyPlanAddBySelf(@RequestBody AddDailyPlanRequest request){
        AddDailyPlanResponse response=new AddDailyPlanResponse();
        List<PlanData> list = new ArrayList<>();
        //获取当前时间
        Timestamp time = new Timestamp(System.currentTimeMillis());
        PlanData planData = new PlanData();
        //插入用户名
        planData.setUsername(request.getUsername());
        //插入主题
        planData.setTitle(request.getTitle());
        //插入会议内容
        planData.setContent(request.getContent());
        //插入会议位置
        planData.setPosition(request.getPosition());
        //插入会议开始时间
        planData.setStarttime(request.getStarttime());
        //插入会议结束时间
        planData.setEndtime(request.getEndtime());
        //插入待办时间
        planData.setPlantime(request.getPlantime());
        //插入创建时间
        planData.setCreatetime(time);
        //插入初始更新时间=创建时间
        planData.setUpdatetime(time);
        //插入日程状态  1-正常，0-禁用 -1,已删除
        planData.setFlag("1");
        //插入数据来源  0：手动添加 1:邮件同步
        planData.setSource("0");
        list.add(planData);
        try {
//            dailyPlanAddBySelfService.dailyPlanAddBySelf(list);
            response.setErrorCode(ErrorCode.SUCCESS);
        }catch(Exception e){
            log.error("",e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }

    /**
     * 删除日程
     * @param request
     * @return
     */
    @PostMapping(value = "/dailyPlan/delete", produces = "application/json;charset=utf-8")
    public DeleteDailyPlanReponse dailyPlanDeleteById(@RequestBody DeleteDailyPlanRequest request ){
        DeleteDailyPlanReponse response=new  DeleteDailyPlanReponse();
        try {
//            dailyPlanAddBySelfService.dailyPlanDeleteById(request.getUsername(),request.getPlan_id());
            response.setErrorCode(ErrorCode.SUCCESS);
        }catch(Exception e){
            log.error("",e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }

    /**
     * 更新日程
     * @param request
     * @return
     */
    @PostMapping(value = "/dailyPlan/update", produces = "application/json;charset=utf-8")
    public UpdateDailyPlanResponse dailyPlanUpdateById(@RequestBody UpdateDailyPlanRequest request){
        UpdateDailyPlanResponse response=new UpdateDailyPlanResponse();
        Timestamp time = new Timestamp(System.currentTimeMillis()); //获取当前时间
        PlanData planData = new PlanData();
        planData.setPlanId(request.getPlanId());     //选定id
        planData.setUsername(request.getUsername());   //插入用户名
        planData.setTitle(request.getTitle());         //插入主题
        planData.setContent(request.getContent());     //插入会议内容
        planData.setPosition(request.getPosition());   //插入会议位置
        planData.setStarttime(request.getStarttime()); //插入会议开始时间
        planData.setEndtime(request.getEndtime());     //插入会议结束时间
        planData.setPlantime(request.getPlantime());   //插入待办时间
        //planData.setCreatetime(time);                //插入创建时间
        planData.setUpdatetime(time);                  //插入更新时间!=创建时间
        planData.setFlag("1");                         //插入日程状态  1-正常，0-禁用 -1,已删除
        planData.setSource("0");                       //插入数据来源  0：手动添加 1:邮件同步
        try {
//            dailyPlanAddBySelfService.dailyPlanUpdateById(planData);
            response.setErrorCode(ErrorCode.SUCCESS);
        }catch(Exception e){
            log.error("",e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }



}
